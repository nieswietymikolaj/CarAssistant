package pl.edu.pb.carassistant.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import pl.edu.pb.carassistant.R;

public class DeleteRefuelingActivity extends AppCompatActivity {

    static final String EXTRA_DELETE_REFUELING_ID = "DELETE_REFUELING_ID";

    TextView refuelingDateTime, refuelingMileage, refuelingCost, refuelingLiters, refuelingPriceLiter;
    Button deleteButton, cancelButton;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    HistoryDatabase historyDatabase;

    List<RefuelingModel> refuelingList;
    RefuelingModel refuelingModel;

    String refuelingId;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_refueling);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getUid();

        Toolbar toolbar = findViewById(R.id.delete_refueling_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refuelingId = getIntent().getExtras().getString(EXTRA_DELETE_REFUELING_ID);

        historyDatabase = HistoryDatabase.getDatabase(this);
        refuelingList = historyDatabase.getRefuelingList();

        for (RefuelingModel refuelingModel : refuelingList) {

            if (refuelingModel.getRefuelingId().equals(refuelingId)) {
                this.refuelingModel = refuelingModel;
            }
        }

        refuelingDateTime = findViewById(R.id.refueling_date_time);
        refuelingMileage = findViewById(R.id.refueling_mileage);
        refuelingCost = findViewById(R.id.refueling_cost);
        refuelingLiters = findViewById(R.id.refueling_liters);
        refuelingPriceLiter = findViewById(R.id.refueling_price_liter);

        refuelingDateTime.setText(refuelingModel.getRefuelingTime() + ", " + refuelingModel.getRefuelingDate());
        refuelingMileage.setText(refuelingModel.getRefuelingMileage() + " km");
        refuelingCost.setText(refuelingModel.getRefuelingCost() + " zł");
        refuelingLiters.setText(refuelingModel.getRefuelingLiters() + " L");
        refuelingPriceLiter.setText(refuelingModel.getRefuelingPriceLiter() + " zł/L");

        deleteButton = findViewById(R.id.history_delete_button);
        cancelButton = findViewById(R.id.history_cancel_button);

        cancelButton.setOnClickListener(v -> {
            onBackPressed();
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("users/" + userId + "/refueling").document(refuelingId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.history_deleted), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.new_user_error) + " " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
}