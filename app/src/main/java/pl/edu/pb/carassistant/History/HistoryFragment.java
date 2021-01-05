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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.pb.carassistant.Dialogs.DeleteAllHistoryDialog;
import pl.edu.pb.carassistant.R;
import pl.edu.pb.carassistant.User.UserDatabase;
import pl.edu.pb.carassistant.User.UserModel;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Activity activity;
    Context context;

    HistoryAdapter historyAdapter;

    HistoryDatabase historyDatabase;
    UserDatabase userDatabase;

    String userId;

    List<RefuelingModel> refuelingList;

    String lastMileage;

    float consumption;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getUid();

        userDatabase = UserDatabase.getDatabase(activity, userId);
        userDatabase.getUserData(userId);

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

    private void NotifyDataLoaded(List<RefuelingModel> list) {
        refuelingList = new ArrayList<>();
        refuelingList = list;

        lastMileage = null;

        int counter = 0;
        float sumAvg = 0;

        for (RefuelingModel refuelingModel : refuelingList) {

            if (lastMileage != null) {
                consumption = (100 * Float.parseFloat(refuelingModel.getRefuelingLiters())) / (Float.parseFloat(refuelingModel.getRefuelingMileage()) - Float.parseFloat(lastMileage));
                sumAvg += consumption;
                counter++;
            }

            lastMileage = refuelingModel.getRefuelingMileage();
        }

        sumAvg = sumAvg/counter;

        UserModel userModel = userDatabase.getUser();

        String avgConsumption = String.format("%.2f", sumAvg);

        if(!userModel.getUserCarAvgConsumption().equals(avgConsumption))
        {
            UpdateAverageConsumption(avgConsumption);
        }

        lastMileage = null;

        historyAdapter.notifyDataSetChanged();
    }

    private class HistoryHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView refuelingDateTime, refuelingMileage, refuelingCost, refuelingLiters, refuelingPriceLiter, refuelingConsumption;
        RefuelingModel refuelingModel;

        float consumption;
        String consumptionText;

        public HistoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_history, parent, false));

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            refuelingDateTime = itemView.findViewById(R.id.refueling_date_time);
            refuelingMileage = itemView.findViewById(R.id.refueling_mileage);
            refuelingCost = itemView.findViewById(R.id.refueling_cost);
            refuelingLiters = itemView.findViewById(R.id.refueling_liters);
            refuelingPriceLiter = itemView.findViewById(R.id.refueling_price_liter);
            refuelingConsumption = itemView.findViewById(R.id.refueling_avg_fuel_consumption);
        }

        public void bind(RefuelingModel refuelingModel) {
            this.refuelingModel = refuelingModel;

            refuelingDateTime.setText(refuelingModel.getRefuelingTime() + ", " + refuelingModel.getRefuelingDate());
            refuelingMileage.setText(refuelingModel.getRefuelingMileage() + " km");
            refuelingCost.setText(refuelingModel.getRefuelingCost() + " zł");
            refuelingLiters.setText(refuelingModel.getRefuelingLiters() + " L");
            refuelingPriceLiter.setText(refuelingModel.getRefuelingPriceLiter() + " zł/L");

            if (lastMileage != null) {
                consumption = (100 * Float.parseFloat(refuelingModel.getRefuelingLiters())) / (Float.parseFloat(refuelingModel.getRefuelingMileage()) - Float.parseFloat(lastMileage));
                consumptionText = String.format("%.2f", consumption);
            } else {
                consumptionText = "-";
            }

            refuelingConsumption.setText(consumptionText + " L/100 km");

            lastMileage = refuelingModel.getRefuelingMileage();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditRefuelingActivity.class);
            intent.putExtra(EditRefuelingActivity.EXTRA_EDIT_REFUELING_ID, refuelingModel.getRefuelingId());
            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            DeleteAllHistoryDialog deleteAllHistoryDialog = new DeleteAllHistoryDialog();
            deleteAllHistoryDialog.showDialog(activity);

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
            if (refuelingList != null) {
                RefuelingModel refuelingModel = refuelingList.get(position);
                holder.bind(refuelingModel);
            } else {
                Log.d("History Fragment", "No refuelings");
            }
        }

        @Override
        public int getItemCount() {
            if (refuelingList != null) {
                return refuelingList.size();
            } else {
                return 0;
            }
        }
    }

    private void UpdateAverageConsumption(String avgConsumption)
    {
        DocumentReference userDocumentReference = firebaseFirestore.collection("users").document(userId);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("AvgConsumption", avgConsumption);

        userDocumentReference.update(userMap).addOnCompleteListener(task -> {
            Toast.makeText(context, getResources().getString(R.string.edit_user_updated), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(context, getResources().getString(R.string.new_refueling_avg_error) + " " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }
}