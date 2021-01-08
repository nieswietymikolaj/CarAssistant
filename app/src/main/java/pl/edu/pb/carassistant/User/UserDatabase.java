package pl.edu.pb.carassistant.User;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDatabase {

    public interface UserDataLoaded {
        void UserDataLoaded();
    }

    public interface UserPhotoLoaded {
        void UserPhotoLoaded(Uri uri);
    }

    public UserDataLoaded userDataLoaded;
    public UserPhotoLoaded userPhotoLoaded;

    static UserDatabase INSTANCE;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;

    Activity activity;
    Context context;

    UserModel userModel;
    String userId;

    private UserDatabase(Activity activity, String userId) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.userId = userId;

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
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
            userModel.setUserCarAvgConsumption(documentSnapshot.getString("AvgConsumption"));

            if (userDataLoaded != null) {
                userDataLoaded.UserDataLoaded();
            }
        });

        getUserPhoto();
    }

    public void getUserPhoto() {
        StorageReference photoReference = storageReference.child("users/" + userId + "/userProfilePhoto");
        photoReference.getDownloadUrl().addOnSuccessListener(uri -> {
            userModel.setUserPhoto(uri);
            if (userPhotoLoaded != null) {
                userPhotoLoaded.UserPhotoLoaded(uri);
            }
        }).addOnFailureListener(e -> {
            userModel.setUserPhoto(Uri.parse("android.resource://" + context.getPackageName() + "/drawable/ic_launcher_foreground_red_car"));
            if (userPhotoLoaded != null) {
                userPhotoLoaded.UserPhotoLoaded(userModel.getUserPhoto());
            }
        });
    }

    public UserModel getUser() {
        return userModel;
    }
}
