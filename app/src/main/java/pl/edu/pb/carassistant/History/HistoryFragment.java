package pl.edu.pb.carassistant.History;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.edu.pb.carassistant.R;

public class HistoryFragment extends Fragment {

    Activity activity;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        FloatingActionButton add = view.findViewById(R.id.floatingActionButton);
        add.setOnClickListener(v -> {
            activity.startActivity(new Intent(context, NewExpenseActivity.class));
        });

        return view;
    }
}