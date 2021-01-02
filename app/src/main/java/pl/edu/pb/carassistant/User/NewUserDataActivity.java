package pl.edu.pb.carassistant.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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
import pl.edu.pb.carassistant.RegisterActivity;

public class NewUserDataActivity extends AppCompatActivity implements TextWatcher {

    EditText userName, carBrand, carModel, carYear, carMileage, carRegistrationNumber;
    Button confirmButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_data);

        Toolbar toolbar = findViewById(R.id.new_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        String email = bundle.getString("NEW_USER_EMAIL");
        String password = bundle.getString("NEW_USER_PASSWORD");

        userName = findViewById(R.id.new_user_name_text);
        carBrand = findViewById(R.id.new_user_car_brand_text);
        carModel = findViewById(R.id.new_user_car_model_text);
        carYear = findViewById(R.id.new_user_car_year_text);
        carMileage = findViewById(R.id.new_user_car_mileage_text);
        carRegistrationNumber = findViewById(R.id.new_user_car_registration_number_text);

        userName.addTextChangedListener(this);
        carBrand.addTextChangedListener(this);
        carModel.addTextChangedListener(this);
        carYear.addTextChangedListener(this);
        carMileage.addTextChangedListener(this);
        carRegistrationNumber.addTextChangedListener(this);

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
        String year = carYear.getText().toString().trim();
        String mileage = carMileage.getText().toString().trim();
        String registration = carRegistrationNumber.getText().toString().trim();

        if (!ValidateUserName(name) || !ValidateCarBrand(brand) || !ValidateCarModel(model) || !ValidateCarYear(year) || !ValidateCarMileage(mileage) || !ValidateCarRegistration(registration)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewUserDataActivity.this, getResources().getString(R.string.new_user_created), Toast.LENGTH_SHORT).show();
                    SaveUserInDatabase(name, brand, model, year, mileage, registration);
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

    private void SaveUserInDatabase(String name, String brand, String model, String year, String mileage, String registration) {
        String userId = firebaseAuth.getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Brand", brand);
        map.put("Model", model);
        map.put("Year", year);
        map.put("Mileage", mileage);
        map.put("Registration", registration);

        documentReference.set(map).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, getResources().getString(R.string.new_user_data_added), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
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

    private boolean ValidateCarMileage(String mileage) {
        if (mileage.isEmpty()) {
            carMileage.setError(getString(R.string.new_user_error_empty));
            carMileage.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (mileage.length() > 7) {
            carMileage.setError(getString(R.string.new_user_error_mileage));
            carMileage.setBackgroundResource(R.drawable.edit_text_error);
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        userName.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carBrand.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carModel.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carYear.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carMileage.setBackground(getDrawable(R.color.edit_text_yellow_background));
        carRegistrationNumber.setBackground(getDrawable(R.color.edit_text_yellow_background));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

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
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}