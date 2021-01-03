package pl.edu.pb.carassistant.User;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDatabase {

    public interface UserDataLoaded {
        void UserDataLoaded();
    }

    public UserDataLoaded userDataLoaded;

    static UserDatabase INSTANCE;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Context context;

    UserModel userModel;
    String userId;

    private UserDatabase(Activity activity, String userId) {

        context = activity.getApplicationContext();
        this.userId = userId;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static UserDatabase getDatabase(Activity activity, String userId) {
        if (INSTANCE == null) {
            INSTANCE = new UserDatabase(activity, userId);
        }
        return INSTANCE;
    }

    public static void clearInstance() {
        INSTANCE = null;
    }

    public void getUserData(String userId)
    {
        userModel = new UserModel(userId);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        documentReference.addSnapshotListener((documentSnapshot, e) -> {
            userModel.setUserName(documentSnapshot.getString("Name"));
            userModel.setUserCarBrand(documentSnapshot.getString("Brand"));
            userModel.setUserCarModel(documentSnapshot.getString("Model"));
            userModel.setUserCarYear(documentSnapshot.getString("Year"));
            userModel.setUserCarMileage(documentSnapshot.getString("Mileage"));
            userModel.setUserCarRegistrationNumber(documentSnapshot.getString("Registration"));

            if (userDataLoaded != null) {
                userDataLoaded.UserDataLoaded();
            }
        });
    }

    public UserModel getUser() {
        return userModel;
    }
}
