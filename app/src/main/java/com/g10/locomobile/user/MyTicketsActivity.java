package com.g10.locomobile.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.HomeActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.SearchResultsActivity;
import com.g10.locomobile.admin.TrainInfoActivity;
import com.g10.locomobile.models.Seat;
import com.g10.locomobile.models.Train;
import com.g10.locomobile.models.User;
import com.g10.locomobile.models.Wagon;
import com.g10.locomobile.ui.main.TrainManagementFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class MyTicketsActivity extends BaseActivity {
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        setTitle("My Train Tickets");

        final ArrayList<Train> trains = new ArrayList<>();
        final ArrayList<Train> tempTrains = new ArrayList<>();
        final ArrayList<String> myTrainNames = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyTicketsActivity.this, android.R.layout.simple_list_item_1, myTrainNames);
        list = (ListView) findViewById(R.id.listView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToNextActivity = new Intent(getApplicationContext(), TrainDetailsActivity.class);
                goToNextActivity.putExtra("train", tempTrains.get(position));
                goToNextActivity.putExtra("KEY_user", "default");
                startActivity(goToNextActivity);
            }
        });

        list.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        uid = user.getUid();

        db = FirebaseFirestore.getInstance();

        FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final User userLocal = document.toObject(User.class);

                        FirebaseFirestore.getInstance().collection("trains")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                Train currentTrain = document.toObject(Train.class);
                                                trains.add(currentTrain);
                                                if (currentTrain.isUserInTrain(userLocal) != null){
                                                    myTrainNames.add(currentTrain.getname());
                                                    System.out.println("FOUNDDD " + currentTrain.getname());
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            for (Train t: trains){
                                                if (myTrainNames.contains(t.getname())){
                                                    tempTrains.add(t);
                                                }
                                            }



                                        } else Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                });

                    } else Log.d(TAG, "No such document");

                } else Log.d(TAG, "get failed with ", task.getException());
            }
        });


    }
}