package pl.edu.pb.carassistant.History;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.edu.pb.carassistant.R;

public class HistoryDatabase {

    public interface HistoryDataLoaded {
        void HistoryDataLoaded(List<RefuelingModel> refuelingList);
    }

    public HistoryDataLoaded historyDataLoaded;

    static HistoryDatabase INSTANCE;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Activity activity;
    Context context;

    List<RefuelingModel> refuelingList;
    RefuelingModel refuelingModel;

    private HistoryDatabase(Activity activity) {

        this.activity = activity;
        context = activity.getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
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

    public void getHistoryData() {

        String userId = firebaseAuth.getUid();

        CollectionReference collectionReference = firebaseFirestore.collection("users/" + userId + "/refueling");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                refuelingList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    refuelingModel = new RefuelingModel();

                    refuelingModel.setRefuelingId(documentSnapshot.getId());
                    refuelingModel.setRefuelingDate(documentSnapshot.getString("Date"));
                    refuelingModel.setRefuelingTime(documentSnapshot.getString("Time"));
                    refuelingModel.setRefuelingMileage(documentSnapshot.getString("Mileage"));
                    refuelingModel.setRefuelingPriceLiter(documentSnapshot.getString("PriceLiter"));
                    refuelingModel.setRefuelingCost(documentSnapshot.getString("Cost"));
                    refuelingModel.setRefuelingLiters(documentSnapshot.getString("Liters"));

                    refuelingList.add(refuelingModel);

                    Collections.sort(refuelingList, new Comparator<RefuelingModel>() {
                        public int compare(RefuelingModel obj1, RefuelingModel obj2) {
                            if (Integer.parseInt(obj1.getRefuelingMileage()) > Integer.parseInt(obj2.getRefuelingMileage())) {
                                return 1;
                            } else if (Integer.parseInt(obj1.getRefuelingMileage()) < Integer.parseInt(obj2.getRefuelingMileage())) {
                                return -1;
                            } else {
                                return 0;
                            }
                            //return obj1.getRefuelingMileage().compareToIgnoreCase(obj2.getRefuelingMileage());
                        }
                    });
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
