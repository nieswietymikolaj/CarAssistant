package pl.edu.pb.carassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import pl.edu.pb.carassistant.User.NewUserDataActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail, registerPassword, registerRepeatPassword;
    TextInputLayout registerTextPassword, registerTextRepeatPassword;
    Button registerButton, goLoginButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        registerEmail = findViewById(R.id.register_email_text);
        registerPassword = findViewById(R.id.register_password_text);
        registerRepeatPassword = findViewById(R.id.register_repeat_password_text);

        registerTextPassword = findViewById(R.id.register_password);
        registerTextRepeatPassword = findViewById(R.id.register_repeat_password);

        TextChangedListeners();

        registerButton = findViewById(R.id.register_button);
        goLoginButton = findViewById(R.id.register_login_button);

        progressBar = findViewById(R.id.register_progress_bar);

        goLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailExistence();
            }
        });
    }

    private void CheckEmailExistence() {
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
            registerTextPassword.setEndIconVisible(false);
            registerPassword.setError(getString(R.string.register_error_short_password));
            registerPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!password.equals(repeatPassword)) {
            registerTextRepeatPassword.setEndIconVisible(false);
            registerRepeatPassword.setError(getString(R.string.register_error_different_password));
            registerRepeatPassword.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private void TextChangedListeners() {
        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerEmail.setBackground(getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (registerPassword.length() > 8 || registerPassword != null) {
                    registerTextPassword.setEndIconVisible(true);
                }
                registerPassword.setBackground(getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (registerRepeatPassword.length() > 8 || registerRepeatPassword != null) {
                    registerTextRepeatPassword.setEndIconVisible(true);
                }
                registerRepeatPassword.setBackground(getDrawable(R.color.edit_text_yellow_background));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}