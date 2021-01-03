package pl.edu.pb.carassistant.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import pl.edu.pb.carassistant.LoginActivity;
import pl.edu.pb.carassistant.R;

import static android.app.Activity.RESULT_OK;

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

    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = firebaseAuth.getUid();

        userName = view.findViewById(R.id.user_name);
        carBrand = view.findViewById(R.id.user_car_brand);
        carModel = view.findViewById(R.id.user_car_model);
        carYear = view.findViewById(R.id.user_car_year);
        carMileage = view.findViewById(R.id.user_car_mileage);
        carRegistrationNumber = view.findViewById(R.id.user_car_registration_number);

        userPhoto = view.findViewById(R.id.user_photo);

        progressBar = view.findViewById(R.id.user_progress_bar);
        photoProgressBar = view.findViewById(R.id.user_photo_progress_bar);

        userPhoto.setOnClickListener(v -> CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL).start(getContext(), this));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoProgressBar.setVisibility(View.VISIBLE);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                SaveUserPhotoInDatabase(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, "Error: " + result.getError(), Toast.LENGTH_LONG).show();
                photoProgressBar.setVisibility(View.INVISIBLE);
            } else {
                photoProgressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            photoProgressBar.setVisibility(View.INVISIBLE);
        }
    }

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

        userDatabase = UserDatabase.getDatabase(activity, userId);
        userDatabase.getUserData(userId);
        userDatabase.userDataLoaded = this::GetUserProfileData;
    }

    private void SaveUserPhotoInDatabase(Uri uri)
    {
        Glide.with(activity).load(uri).placeholder(R.drawable.ic_launcher_foreground_red_car).error(R.drawable.ic_broken_image_24).into(userPhoto);

        userModel = userDatabase.getUser();

        userModel.setUserPhoto(uri);

        StorageReference photoReference = storageReference.child("users/" + userId + "/userProfilePhoto");
        photoReference.putFile(userModel.getUserPhoto()).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(context, getResources().getString(R.string.user_photo_added), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, getResources().getString(R.string.new_user_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void GetUserProfileData() {

        userModel = userDatabase.getUser();

        userName.setText(userModel.getUserName());
        carBrand.setText(userModel.getUserCarBrand());
        carModel.setText(userModel.getUserCarModel());
        carYear.setText(userModel.getUserCarYear());
        carMileage.setText(userModel.getUserCarMileage() + " km");
        carRegistrationNumber.setText(userModel.getUserCarRegistrationNumber());

        progressBar.setVisibility(View.INVISIBLE);
    }
}