package pl.edu.pb.carassistant.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import pl.edu.pb.carassistant.R;

public class ChangePasswordDialog {

    EditText oldPassword;
    TextInputLayout oldTextPassword;
    Button changeButton;

    Activity activity;
    Context context;

    FirebaseAuth firebaseAuth;

    public void showDialog(Activity activity) {

        this.activity = activity;
        context = activity.getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();

        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_change_password);

        oldPassword = dialog.findViewById(R.id.change_password_old_password_text);

        oldTextPassword = dialog.findViewById(R.id.change_password_old_password);

        TextChangedListener();

        changeButton = dialog.findViewById(R.id.change_password_button);

        oldPassword.requestFocus();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = firebaseAuth.getCurrentUser().getEmail();
                String password = oldPassword.getText().toString().trim();

                if (!ValidatePassword(password)) {
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, activity.getResources().getString(R.string.reset_password_sent), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, activity.getResources().getString(R.string.new_user_error) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, activity.getResources().getString(R.string.change_password_error_password) + " " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        dialog.show();
    }

    private void TextChangedListener()
    {
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(oldPassword.length() > 8 || oldPassword != null)
                {
                    oldTextPassword.setEndIconVisible(true);
                }
                oldPassword.setBackground(activity.getResources().getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean ValidatePassword(String password) {
        if (password.length() < 8) {
            oldTextPassword.setEndIconVisible(false);
            oldPassword.setError(activity.getResources().getString(R.string.register_error_short_password));
            oldPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }
}
