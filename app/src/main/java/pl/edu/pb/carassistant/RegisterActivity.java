package pl.edu.pb.carassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import pl.edu.pb.carassistant.User.NewUserDataActivity;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    EditText registerEmail, registerPassword, registerRepeatPassword;
    Button registerButton, goLoginButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerRepeatPassword = findViewById(R.id.register_repeat_password);

        registerEmail.addTextChangedListener(this);
        registerPassword.addTextChangedListener(this);
        registerRepeatPassword.addTextChangedListener(this);

        registerButton = findViewById(R.id.register_button);
        goLoginButton = findViewById(R.id.register_login_button);

        progressBar = findViewById(R.id.register_progress_bar);

        goLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String repeatPassword = registerRepeatPassword.getText().toString().trim();

            if (!ValidateEmail(email) || !ValidatePassword(password, repeatPassword)) {
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.isSuccessful()) {
                        List<String> methods = task.getResult().getSignInMethods();
                        if (!methods.isEmpty()) {
                            registerEmail.setError(getString(R.string.register_error_existing_email));
                            registerEmail.setBackgroundResource(R.drawable.edit_text_error);
                            registerEmail.requestFocus();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), NewUserDataActivity.class);
                            intent.putExtra("NEW_USER_EMAIL", email);
                            intent.putExtra("NEW_USER_PASSWORD", password);
                            startActivity(intent);
                            finish();
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        });
    }

    private boolean ValidateEmail(String email) {
        if (email.isEmpty()) {
            registerEmail.setError(getString(R.string.register_error_empty_email));
            registerEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerEmail.setError(getString(R.string.register_error_incorrect_email));
            registerEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidatePassword(String password, String repeatPassword) {
        if (password.length() < 8) {
            registerPassword.setError(getString(R.string.register_error_short_password));
            registerPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!password.equals(repeatPassword)) {
            registerRepeatPassword.setError(getString(R.string.register_error_different_password));
            registerRepeatPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        registerEmail.setBackground(getDrawable(R.color.edit_text_yellow_background));
        registerPassword.setBackground(getDrawable(R.color.edit_text_yellow_background));
        registerRepeatPassword.setBackground(getDrawable(R.color.edit_text_yellow_background));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}