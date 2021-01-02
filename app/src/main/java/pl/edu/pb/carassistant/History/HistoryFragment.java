package pl.edu.pb.carassistant.History;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    RecyclerView recyclerView;

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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(v -> {
            activity.startActivity(new Intent(context, NewRefuelingActivity.class));
        });

        recyclerView = view.findViewById(R.id.refueling_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        return view;
    }
}