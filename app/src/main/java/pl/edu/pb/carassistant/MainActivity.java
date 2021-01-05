package pl.edu.pb.carassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import pl.edu.pb.carassistant.User.UserDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    UserDatabase userDatabase;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        firebaseAuth = FirebaseAuth.getInstance();

        userId = firebaseAuth.getUid();

        userDatabase = UserDatabase.getDatabase(this, userId);
        userDatabase.getUserData(userId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        fragment.getChildFragmentManager().getFragments().get(0).onActivityResult(requestCode, resultCode, data);
    }
}