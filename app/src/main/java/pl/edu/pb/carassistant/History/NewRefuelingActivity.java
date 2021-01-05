package pl.edu.pb.carassistant.History;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pl.edu.pb.carassistant.R;
import pl.edu.pb.carassistant.User.UserDatabase;
import pl.edu.pb.carassistant.User.UserModel;

public class NewRefuelingActivity extends AppCompatActivity implements TextWatcher {

    String DATE_PATTERN = "^(0[1-9]|[1-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/[0-9]{4}$";
    String TIME_PATTERN = "^([0-1][0-9]|2[0-3]):([0-5][0-9])$";
    String PRICE_LITERS_PATTERN = "^(([0-9]|[0-9]{2}|[0-9]{3}|[0-9]{4}|[0-9]{5}|[0-9]{6}).([0-9]|[0-9]{2}|0[0-9]))|([0-9]|[0-9]{2}|[0-9]{3}|[0-9]{4}|[0-9]{5}|[0-9]{6}|[0-9]{7}|[0-9]{8})$";

    EditText refuelingDate, refuelingTime, refuelingMileage, refuelingPriceLiter, refuelingCost, refuelingLiters;
    Button saveButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Calendar calendar;
    SimpleDateFormat dateFormat, timeFormat;
    String currentDate, currentTime;

    UserDatabase userDatabase;
    UserModel userModel;

    String compareMileage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refueling);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userDatabase = UserDatabase.getDatabase(this, firebaseAuth.getUid());
        userModel = userDatabase.getUser();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = dateFormat.format(calendar.getTime());
        timeFormat = new SimpleDateFormat("HH:mm");
        currentTime = timeFormat.format(calendar.getTime());

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

        refuelingDate.setText(currentDate);
        refuelingTime.setText(currentTime);
        refuelingMileage.setText(userModel.getUserCarMileage());

        compareMileage = userModel.getUserCarMileage();

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

        String userId = firebaseAuth.getUid();

        String id = firebaseFirestore.collection("users/" + userId + "/refueling").document().getId();
        DocumentReference documentReference = firebaseFirestore.collection("users/" + userId + "/refueling").document(id);

        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewRefuelingActivity.this, getResources().getString(R.string.new_refueling_created), Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(mileage) > Integer.parseInt(compareMileage)) {
                    UpdateUserMileage(userId, mileage);
                }
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

    private void UpdateUserMileage(String userId, String mileage)
    {
        DocumentReference userDocumentReference = firebaseFirestore.collection("users").document(userId);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Mileage", mileage);

        userDocumentReference.update(userMap).addOnCompleteListener(task -> {
            //Toast.makeText(this, getResources().getString(R.string.edit_user_updated), Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

    private boolean ValidateDate(String date) {
        if (date.isEmpty()) {
            refuelingDate.setError(getString(R.string.new_user_error_empty));
            refuelingDate.setBackgroundResource(R.drawable.edit_text_error);
            return false;
        }

        if (!date.matches(DATE_PATTERN)) {
            refuelingDate.setError(getString(R.string.new_refueling_date_error));
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

        if (!time.matches(TIME_PATTERN)) {
            refuelingTime.setError(getString(R.string.new_refueling_time_error));
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

        if (Integer.parseInt(mileage) < Integer.parseInt(compareMileage)) {
            refuelingMileage.setError(getString(R.string.new_refueling_compare_error) + " (" + compareMileage + " km)");
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

        if (!priceLiter.matches(PRICE_LITERS_PATTERN)) {
            refuelingPriceLiter.setError(getString(R.string.new_refueling_price_liters_error));
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

        if (!cost.matches(PRICE_LITERS_PATTERN)) {
            refuelingCost.setError(getString(R.string.new_refueling_price_liters_error));
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

        if (!liters.matches(PRICE_LITERS_PATTERN)) {
            refuelingLiters.setError(getString(R.string.new_refueling_price_liters_error));
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
        /*float priceLiterNumber = Float.parseFloat(refuelingPriceLiter.getText().toString().trim());
        float costNumber = Float.parseFloat(refuelingCost.getText().toString().trim());
        float litersNumber = Float.parseFloat(refuelingLiters.getText().toString().trim());

        double calculatedValue  = ( (double ) costNumber/priceLiterNumber);

        refuelingLiters.setText(Double.toString(calculatedValue));*/
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