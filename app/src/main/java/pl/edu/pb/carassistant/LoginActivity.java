package pl.edu.pb.carassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import pl.edu.pb.carassistant.Dialogs.ForgotPasswordDialog;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    TextInputLayout loginTextPassword;
    Button loginButton, forgotPasswordButton, goRegisterButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        loginEmail = findViewById(R.id.login_email_text);
        loginPassword = findViewById(R.id.login_password_text);

        loginTextPassword = findViewById(R.id.login_password);

        TextChangedListeners();

        loginButton = findViewById(R.id.login_button);
        forgotPasswordButton = findViewById(R.id.login_forgot_button);
        goRegisterButton = findViewById(R.id.login_register_button);

        progressBar = findViewById(R.id.login_progress_bar);

        goRegisterButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        forgotPasswordButton.setOnClickListener(v -> {
            ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
            forgotPasswordDialog.showDialog(LoginActivity.this);
        });
    }

    private void LoginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (!ValidateEmail(email) || !ValidatePassword(password)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + task.getException(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        });
    }

    private boolean ValidateEmail(String email) {
        if (email.isEmpty()) {
            loginEmail.setError(getString(R.string.register_error_empty_email));
            loginEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError(getString(R.string.register_error_incorrect_email));
            loginEmail.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidatePassword(String password) {
        if (password.length() < 8) {
            loginTextPassword.setEndIconVisible(false);
            loginPassword.setError(getString(R.string.register_error_short_password));
            loginPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private void TextChangedListeners() {
        loginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginEmail.setBackground(getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (loginPassword.length() > 8 || loginPassword != null) {
                    loginTextPassword.setEndIconVisible(true);
                }
                loginPassword.setBackground(getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}