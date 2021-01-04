package pl.edu.pb.carassistant.History;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pb.carassistant.R;

public class HistoryDatabase {

    public interface HistoryDataLoaded {
        void HistoryDataLoaded(List<RefuelingModel> refuelingList);
    }

    public HistoryDataLoaded historyDataLoaded;

    static HistoryDatabase INSTANCE;

    FirebaseFirestore firebaseFirestore;

    Activity activity;
    Context context;

    List<RefuelingModel> refuelingList;
    RefuelingModel refuelingModel;

    private HistoryDatabase(Activity activity) {

        this.activity = activity;
        context = activity.getApplicationContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static HistoryDatabase getDatabase(Activity activity) {
        if (INSTANCE == null) {
            INSTANCE = new HistoryDatabase(activity);
        }
        return INSTANCE;
    }

    public static void clearInstance() {
        INSTANCE = null;
    }

    public void getHistoryData()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("refueling");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                refuelingList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    refuelingModel = new RefuelingModel();

                    refuelingModel.refuelingId = documentSnapshot.getId();
                    refuelingModel.refuelingDate = documentSnapshot.getString("Date");
                    refuelingModel.refuelingTime = documentSnapshot.getString("Time");
                    refuelingModel.refuelingMileage = documentSnapshot.getString("Mileage");
                    refuelingModel.refuelingPriceLiter = documentSnapshot.getString("PriceLiter");
                    refuelingModel.refuelingCost = documentSnapshot.getString("Cost");
                    refuelingModel.refuelingLiters = documentSnapshot.getString("Liters");

                    refuelingList.add(refuelingModel);
                }
                if (historyDataLoaded != null) {
                    historyDataLoaded.HistoryDataLoaded(refuelingList);
                }
            } else {
                Toast.makeText(context, activity.getResources().getString(R.string.new_user_error) + " " + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<RefuelingModel> getRefuelingList() {
        return refuelingList;
    }
}
