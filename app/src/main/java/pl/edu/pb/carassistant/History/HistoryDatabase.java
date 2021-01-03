package pl.edu.pb.carassistant.History;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pl.edu.pb.carassistant.User.UserDatabase;
import pl.edu.pb.carassistant.User.UserModel;

public class HistoryDatabase {

    public interface HistoryDataLoaded {
        void HistoryDataLoaded();
    }

    public HistoryDataLoaded historyDataLoaded;

    static HistoryDatabase INSTANCE;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;

    Activity activity;
    Context context;

    RefuelingModel refuelingModel;
    String userId;

    private HistoryDatabase(Activity activity, String userId) {

        this.activity = activity;
        context = activity.getApplicationContext();
        this.userId = userId;

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static HistoryDatabase getDatabase(Activity activity, String userId) {
        if (INSTANCE == null) {
            INSTANCE = new HistoryDatabase(activity, userId);
        }
        return INSTANCE;
    }

    public static void clearInstance() {
        INSTANCE = null;
    }

    public void getHistoryData(String userId)
    {

            if (historyDataLoaded != null) {
                historyDataLoaded.HistoryDataLoaded();
            }
    }

    public RefuelingModel getRefueling() {
        return refuelingModel;
    }
}
