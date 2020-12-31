package pl.edu.pb.carassistant.Dialogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import pl.edu.pb.carassistant.LoginActivity;
import pl.edu.pb.carassistant.R;
import pl.edu.pb.carassistant.User.NewUserDataActivity;

public class ResetPasswordDialog {

    EditText resetEmail;
    Button resetButton;

    Activity activity;

    FirebaseAuth firebaseAuth;

    public void showDialog(Activity activity) {

        this.activity = activity;

        firebaseAuth = FirebaseAuth.getInstance();

        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_reset_password);

        resetEmail = dialog.findViewById(R.id.reset_password_email_text);
        resetButton = dialog.findViewById(R.id.reset_password_button);
        resetEmail.requestFocus();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString().trim();

                if (!ValidateEmail(email)) {
                    return;
                }

                firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            List<String> methods = task.getResult().getSignInMethods();
                            if (methods.isEmpty()) {
                                resetEmail.setError(activity.getResources().getString(R.string.reset_password_error_email));
                                resetEmail.setBackgroundResource(R.drawable.edit_text_error);
                                resetEmail.requestFocus();
                            } else {
                                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.reset_password_sent), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.new_user_error) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        /*dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });*/

        dialog.show();
    }

    private boolean ValidateEmail(String email) {
        if (email.isEmpty()) {
            resetEmail.setError(activity.getResources().getString(R.string.register_error_empty_email).toString());
            resetEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetEmail.setError(activity.getResources().getString(R.string.register_error_incorrect_email));
            resetEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }
}