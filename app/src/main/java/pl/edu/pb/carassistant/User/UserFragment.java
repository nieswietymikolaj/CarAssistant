package pl.edu.pb.carassistant.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import pl.edu.pb.carassistant.LoginActivity;
import pl.edu.pb.carassistant.R;

public class UserFragment extends Fragment {

    TextView userName, carBrand, carModel, carYear, carMileage, carRegistrationNumber;
    ImageView userPhoto;

    FirebaseAuth firebaseAuth;
    StorageReference storageReference;

    ProgressBar progressBar, photoProgressBar;

    Activity activity;
    Context context;

    UserDatabase userDatabase;
    UserModel userModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = getContext();  /*activity.getApplicationContext();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userName = view.findViewById(R.id.user_name);
        carBrand = view.findViewById(R.id.user_car_brand);
        carModel = view.findViewById(R.id.user_car_model);
        carYear = view.findViewById(R.id.user_car_year);
        carMileage = view.findViewById(R.id.user_car_mileage);
        carRegistrationNumber = view.findViewById(R.id.user_car_registration_number);

        userPhoto = view.findViewById(R.id.user_photo);

        progressBar = view.findViewById(R.id.user_progress_bar);

/*        userPhoto.setOnClickListener(view1 -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL)
                .start(getActivity()));*/

        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = activity.findViewById(R.id.user_toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /*private void SaveUserPhotoInDatabase()
    {
        StorageReference photoReference = storageReference.child("users/" + userId + "/profile.jpg");
        photoReference.putFile(user.getProfileImage()).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(context, "Photo has been loaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        });
    }*/

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user_data_edit:
                startActivity(new Intent(context, EditUserDataActivity.class));
                return true;

            case R.id.user_logout:
                FirebaseAuth.getInstance().signOut();
                UserDatabase.clearInstance();
                startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        userDatabase = UserDatabase.getDatabase(activity, firebaseAuth.getUid());
        userDatabase.getUserData(firebaseAuth.getUid());
        userDatabase.userDataLoaded = this::GetUserProfileData;
    }

    private void GetUserProfileData() {

        userModel = userDatabase.getUser();

        userName.setText(userModel.getUserName());
        carBrand.setText(userModel.getUserCarBrand());
        carModel.setText(userModel.getUserCarModel());
        carYear.setText(userModel.getUserCarYear());
        carMileage.setText(userModel.getUserCarMileage());
        carRegistrationNumber.setText(userModel.getUserCarRegistrationNumber());

        progressBar.setVisibility(View.INVISIBLE);
    }
}