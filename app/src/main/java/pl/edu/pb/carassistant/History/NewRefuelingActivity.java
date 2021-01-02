package pl.edu.pb.carassistant.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.edu.pb.carassistant.MainActivity;
import pl.edu.pb.carassistant.R;

public class NewRefuelingActivity extends AppCompatActivity implements TextWatcher {

    EditText refuelingDate, refuelingTime, refuelingMileage, refuelingPriceLiter, refuelingCost, refuelingLiters;
    Button saveButton;
    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refueling);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.new_refueling_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refuelingDate = findViewById(R.id.new_refueling_date_text);
        refuelingTime = findViewById(R.id.new_refueling_time_text);
        refuelingMileage = findViewById(R.id.new_refueling_mileage_text);
        refuelingPriceLiter = findViewById(R.id.new_refueling_price_liter_text);
        refuelingCost = findViewById(R.id.new_refueling_cost_text);
        refuelingLiters = findViewById(R.id.new_refueling_liters_text);

        refuelingDate.addTextChangedListener(this);
        refuelingTime.addTextChangedListener(this);
        refuelingMileage.addTextChangedListener(this);
        refuelingPriceLiter.addTextChangedListener(this);
        refuelingCost.addTextChangedListener(this);
        refuelingLiters.addTextChangedListener(this);

        saveButton = findViewById(R.id.new_refueling_save_button);

        progressBar = findViewById(R.id.new_refueling_progress_bar);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewRefueling();
            }
        });
    }

    /*private void SaveUserInDatabase(String name, String brand, String model, String year, String mileage, String registration) {
        String userId = firebaseAuth.getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);


        documentReference.set(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, getResources().getString(R.string.new_user_data_added), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }*/



    /*firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    });*/

    private void AddNewRefueling() {
        String date = refuelingDate.getText().toString().trim();
        String time = refuelingTime.getText().toString().trim();
        String mileage = refuelingMileage.getText().toString().trim();
        String priceLiter = refuelingPriceLiter.getText().toString().trim();
        String cost = refuelingCost.getText().toString().trim();
        String liters = refuelingLiters.getText().toString().trim();

        if (!ValidateDate(date) || !ValidateTime(time) || !ValidateMileage(mileage) || !ValidatePriceLiter(priceLiter) || !ValidateCost(cost) || !ValidateLiters(liters)) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Date", date);
        map.put("Time", time);
        map.put("Mileage", mileage);
        map.put("PriceLiter", priceLiter);
        map.put("Cost", cost);
        map.put("Liters", liters);

        progressBar.setVisibility(View.VISIBLE);

        String id = firebaseFirestore.collection("refueling").document().getId();
        DocumentReference documentReference = firebaseFirestore.collection("refueling").document(id);

        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewRefuelingActivity.this, getResources().getString(R.string.new_refueling_created), Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewRefuelingActivity.this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean ValidateDate(String date) {
        if (date.isEmpty()) {
            refuelingDate.setError(getString(R.string.new_user_error_empty));
            refuelingDate.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateTime(String time) {
        if (time.isEmpty()) {
            refuelingTime.setError(getString(R.string.new_user_error_empty));
            refuelingTime.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateMileage(String mileage) {
        if (mileage.isEmpty()) {
            refuelingMileage.setError(getString(R.string.new_user_error_empty));
            refuelingMileage.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidatePriceLiter(String priceLiter) {
        if (priceLiter.isEmpty()) {
            refuelingPriceLiter.setError(getString(R.string.new_user_error_empty));
            refuelingPriceLiter.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateCost(String cost) {
        if (cost.isEmpty()) {
            refuelingCost.setError(getString(R.string.new_user_error_empty));
            refuelingCost.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    private boolean ValidateLiters(String liters) {
        if (liters.isEmpty()) {
            refuelingLiters.setError(getString(R.string.new_user_error_empty));
            refuelingLiters.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        refuelingDate.setBackground(getDrawable(R.color.edit_text_yellow_background));
        refuelingTime.setBackground(getDrawable(R.color.edit_text_yellow_background));
        refuelingMileage.setBackground(getDrawable(R.color.edit_text_yellow_background));
        refuelingPriceLiter.setBackground(getDrawable(R.color.edit_text_yellow_background));
        refuelingCost.setBackground(getDrawable(R.color.edit_text_yellow_background));
        refuelingLiters.setBackground(getDrawable(R.color.edit_text_yellow_background));
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