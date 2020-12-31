package pl.edu.pb.carassistant.Dialogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import pl.edu.pb.carassistant.R;

public class ResetPasswordDialog {

    EditText resetEmail;
    Button resetButton;

    Activity activity;

    public void showDialog(Activity activity) {

        this.activity = activity;

        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reset_password);

        resetEmail = (EditText) dialog.findViewById(R.id.reset_password_email);
        resetButton = (Button) dialog.findViewById(R.id.reset_password_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString().trim();

                if (!ValidateEmail(email)) {
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.reset_password_sent), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.new_user_error) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                dialog.dismiss();
            }
        });

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