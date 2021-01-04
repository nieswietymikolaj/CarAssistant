package pl.edu.pb.carassistant.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.edu.pb.carassistant.Dialogs.ChangePasswordDialog;
import pl.edu.pb.carassistant.R;

public class EditUserDataActivity extends AppCompatActivity implements TextWatcher {

    EditText userName, carBrand, carModel, carYear, carMileage, carRegistrationNumber;
    Button saveButton, changePasswordButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    UserDatabase userDatabase;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userDatabase = UserDatabase.getDatabase(this, firebaseAuth.getUid());
        userModel = userDatabase.getUser();

        Toolbar toolbar = findViewById(R.id.edit_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.user_name_text);
        carBrand = findViewById(R.id.user_car_brand_text);
        carModel = findViewById(R.id.user_car_model_text);
        carYear = findViewById(R.id.user_car_year_text);
        carMileage = findViewById(R.id.user_car_mileage_text);
        carRegistrationNumber = findViewById(R.id.user_car_registration_number_text);

        userName.setText(userModel.getUserName());
        carBrand.setText(userModel.getUserCarBrand());
        carModel.setText(userModel.getUserCarModel());
        carYear.setText(userModel.getUserCarYear());
        carMileage.setText(userModel.getUserCarMileage());
        carRegistrationNumber.setText(userModel.getUserCarRegistrationNumber());

        userName.addTextChangedListener(this);
        carBrand.addTextChangedListener(this);
        carModel.addTextChangedListener(this);
        carYear.addTextChangedListener(this);
        carMileage.addTextChangedListener(this);
        carRegistrationNumber.addTextChangedListener(this);

        saveButton = findViewById(R.id.edit_user_save_button);
        changePasswordButton = findViewById(R.id.edit_change_password_button);

        progressBar = findViewById(R.id.edit_user_progress_bar);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserData();
            }
        });

        changePasswordButton.setOnClickListener(v -> {
            ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
            changePasswordDialog.showDialog(EditUserDataActivity.this);
        });
    }

    private void UpdateUserData()
    {
        String name = userName.getText().toString().trim();
        String brand = carBrand.getText().toString().trim();
        String model = carModel.getText().toString().trim();
        String year = carYear.getText().toString().trim();
        String mileage = carMileage.getText().toString().trim();
        String registration = carRegistrationNumber.getText().toString().trim();

        if (!ValidateUserName(name) || !ValidateCarBrand(brand) || !ValidateCarModel(model) || !ValidateCarYear(year) || !ValidateCarMileage(mileage) || !ValidateCarRegistration(registration)) {
            return;
        }

        userModel.setUserName(userName.getText().toString());
        userModel.setUserCarBrand(carBrand.getText().toString());
        userModel.setUserCarModel(carModel.getText().toString());
        userModel.setUserCarYear(carYear.getText().toString());
        userModel.setUserCarMileage(carMileage.getText().toString());
        userModel.setUserCarRegistrationNumber(carRegistrationNumber.getText().toString());

        progressBar.setVisibility(View.VISIBLE);

        String userId = firebaseAuth.getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("Name", userModel.getUserName());
        map.put("Brand", userModel.getUserCarBrand());
        map.put("Model", userModel.getUserCarModel());
        map.put("Year", userModel.getUserCarYear());
        map.put("Mileage", userModel.getUserCarMileage());
        map.put("Registration", userModel.getUserCarRegistrationNumber());

        documentReference.update(map).addOnCompleteListener(task -> {
            Toast.makeText(this, getResources().getString(R.string.edit_user_updated), Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

        progressBar.setVisibility(View.INVISIBLE);
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
}