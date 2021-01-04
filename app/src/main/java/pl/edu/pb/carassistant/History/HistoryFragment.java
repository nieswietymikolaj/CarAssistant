package pl.edu.pb.carassistant.History;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pb.carassistant.R;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseFirestore firebaseFirestore;

    Activity activity;
    Context context;

    HistoryAdapter historyAdapter;

    HistoryDatabase historyDatabase;

    List<RefuelingModel> refuelingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(v -> {
            activity.startActivity(new Intent(context, NewRefuelingActivity.class));
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.refueling_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        historyDatabase = HistoryDatabase.getDatabase(activity);
        historyDatabase.getHistoryData();
        historyDatabase.historyDataLoaded = this::NotifyDataLoaded;
    }

    private void NotifyDataLoaded(List<RefuelingModel> list)
    {
        refuelingList = new ArrayList<>();
        refuelingList = list;

        historyAdapter.notifyDataSetChanged();
    }

    private class HistoryHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView refuelingDateTime, refuelingMileage, refuelingCost, refuelingLiters;
        RefuelingModel refuelingModel;

        public HistoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_history, parent, false));

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            refuelingDateTime = itemView.findViewById(R.id.refueling_date_time);
            refuelingMileage = itemView.findViewById(R.id.refueling_mileage);
            refuelingCost = itemView.findViewById(R.id.refueling_cost);
            refuelingLiters = itemView.findViewById(R.id.refueling_liters);
        }

        public void bind(RefuelingModel refuelingModel) {
            this.refuelingModel = refuelingModel;

            refuelingDateTime.setText(refuelingModel.getRefuelingTime() + " , " + refuelingModel.getRefuelingDate());
            refuelingMileage.setText(refuelingModel.getRefuelingMileage() + " km");
            refuelingCost.setText(refuelingModel.getRefuelingCost() + " zł");
            refuelingLiters.setText(refuelingModel.getRefuelingLiters() + " L");
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

        @NonNull
        @Override
        public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HistoryHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
            if(refuelingList != null) {
                RefuelingModel refuelingModel = refuelingList.get(position);
                holder.bind(refuelingModel);
            } else {
                Log.d("History Fragment", "No refuelings");
            }
        }

        @Override
        public int getItemCount() {
            if(refuelingList != null) {
                return refuelingList.size();
            } else {
                return 0;
            }
        }
    }
}