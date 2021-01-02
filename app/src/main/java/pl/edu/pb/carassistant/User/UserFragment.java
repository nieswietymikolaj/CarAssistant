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

import com.google.firebase.auth.FirebaseAuth;

import pl.edu.pb.carassistant.LoginActivity;
import pl.edu.pb.carassistant.R;

public class UserFragment extends Fragment {

    //Button logoutButton;

    Activity activity;
    Context context;

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

/*        logoutButton = view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            activity.finish();
        });*/

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
                startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}