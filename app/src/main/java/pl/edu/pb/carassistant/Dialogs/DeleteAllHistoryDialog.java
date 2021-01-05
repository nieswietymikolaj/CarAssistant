package pl.edu.pb.carassistant.Dialogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.edu.pb.carassistant.History.HistoryDatabase;
import pl.edu.pb.carassistant.History.RefuelingModel;
import pl.edu.pb.carassistant.R;

public class DeleteAllHistoryDialog extends AppCompatActivity {

    Button deleteButton, cancelButton;

    Activity activity;
    Context context;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userId;

    public void showDialog(Activity activity) {

        this.activity = activity;
        context = activity.getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getUid();

        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_delete_all_history_dialog);

        deleteButton = dialog.findViewById(R.id.history_delete_button);
        cancelButton = dialog.findViewById(R.id.history_cancel_button);

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("users/" + userId).document("refueling");

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, activity.getResources().getString(R.string.history_deleted), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, activity.getResources().getString(R.string.new_user_error) + " " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        dialog.show();
    }
}