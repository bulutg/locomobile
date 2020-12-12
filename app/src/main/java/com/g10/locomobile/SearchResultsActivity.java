package com.g10.locomobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.g10.locomobile.models.Train;
import com.g10.locomobile.models.TrainSystem;
import com.g10.locomobile.user.SeatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchResultsActivity extends BaseActivity {

    String departure, destination, date;
    ArrayList<Train> trains, searchResults;
    ArrayList<String> trainNames;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        trains = new ArrayList<>();

        departure = getIntent().getStringExtra("KEY_departure");
        destination = getIntent().getStringExtra("KEY_destination");
        date = getIntent().getStringExtra("KEY_date");

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
                            }

                            trainNames = new ArrayList<>();

                            Log.d(TAG, "UPDATED TRAIN DB ");
                            System.out.println(trains.toString());

                            ArrayList<Train> trainArrayList = (TrainSystem.searchTrain(departure, destination, trains, date));





                            ArrayAdapter<Train> adapter = new ArrayAdapter<Train>(SearchResultsActivity.this, R.layout.support_simple_spinner_dropdown_item, trainArrayList);

                            list = (ListView) findViewById(R.id.results);
                            list.setAdapter(adapter);

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = ((Train) parent.getItemAtPosition(position)).getname();

                                    int end = name.indexOf("Time:");

                                    Intent goToNextActivity = new Intent(getApplicationContext(), SeatActivity.class);
                                    goToNextActivity.putExtra("trainName", name.trim());
//                                    System.out.println("ABOOOOOO " + name.substring(0, end));
                                    goToNextActivity.putExtra("KEY_departure", departure);
                                    goToNextActivity.putExtra("KEY_destination", destination);
                                    startActivity(goToNextActivity);
                                }
                            });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
