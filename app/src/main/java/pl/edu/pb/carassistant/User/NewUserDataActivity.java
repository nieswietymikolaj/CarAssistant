package pl.edu.pb.carassistant.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.edu.pb.carassistant.MainActivity;
import pl.edu.pb.carassistant.R;

public class NewUserDataActivity extends AppCompatActivity implements TextWatcher {

    EditText userName, carBrand, carModel, carRegistrationNumber, carYear;
    Button confirmButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_data);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        String email = bundle.getString("NEW_USER_EMAIL");
        String password = bundle.getString("NEW_USER_PASSWORD");

        userName = findViewById(R.id.new_user_name);
        carBrand = findViewById(R.id.new_user_car_brand);
        carModel = findViewById(R.id.new_user_car_model);
        carRegistrationNumber = findViewById(R.id.new_user_car_registration_number);
        carYear = findViewById(R.id.new_user_car_year);

        userName.addTextChangedListener(this);
        carBrand.addTextChangedListener(this);
        carModel.addTextChangedListener(this);
        carRegistrationNumber.addTextChangedListener(this);
        carYear.addTextChangedListener(this);

        confirmButton = findViewById(R.id.new_user_confirm_button);

        progressBar = findViewById(R.id.new_user_progress_bar);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser(email, password);
            }
        });
    }

    private void RegisterUser(String email, String password) {

        String name = userName.getText().toString().trim();
        String brand = carBrand.getText().toString().trim();
        String model = carModel.getText().toString().trim();
        String registration = carRegistrationNumber.getText().toString().trim();
        String year = carYear.getText().toString().trim();

        if (!ValidateUserName(name) || !ValidateCarBrand(brand) || !ValidateCarModel(model) || !ValidateCarRegistration(registration) || !ValidateCarYear(year)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewUserDataActivity.this, getResources().getString(R.string.new_user_created), Toast.LENGTH_SHORT).show();
                    SaveUserInDatabase(name, brand, model, registration, year);
                } else {
                    Toast.makeText(NewUserDataActivity.this, getResources().getString(R.string.new_user_error) + " " + task.getException(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewUserDataActivity.this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void SaveUserInDatabase(String name, String brand, String model, String registration, String year) {
        String userId = firebaseAuth.getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Brand", brand);
        user.put("Model", model);
        user.put("Registration", registration);
        user.put("Year", year);

        documentReference.set(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.new_user_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

    private boolean ValidateUserName(String name) {
        if (name.isEmpty()) {
            userName.setError(getString(R.string.new_user_error_empty));
            userName.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateCarBrand(String brand) {
        if (brand.isEmpty()) {
            carBrand.setError(getString(R.string.new_user_error_empty));
            carBrand.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateCarModel(String model) {
        if (model.isEmpty()) {
            carModel.setError(getString(R.string.new_user_error_empty));
            carModel.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateCarRegistration(String registration) {
        if (registration.isEmpty()) {
            carRegistrationNumber.setError(getString(R.string.new_user_error_empty));
            carRegistrationNumber.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateCarYear(String year) {
        if (year.isEmpty()) {
            carYear.setError(getString(R.string.new_user_error_empty));
            carYear.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (year.length() > 4) {
            carYear.setError(getString(R.string.new_user_error_year));
            carYear.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        userName.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carBrand.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carModel.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carRegistrationNumber.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carYear.setBackground(getDrawable(R.color.edit_text_yellow_background));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}