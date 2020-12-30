package pl.edu.pb.carassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail, registerPassword, registerRepeatPassword;
    Button registerButton, goLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerRepeatPassword = findViewById(R.id.register_repeat_password);

        registerButton = findViewById(R.id.register_button);
        goLoginButton = findViewById(R.id.register_login_button);

        goLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}