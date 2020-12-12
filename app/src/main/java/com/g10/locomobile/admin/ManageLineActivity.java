package com.g10.locomobile.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.Line;
import com.g10.locomobile.models.Stop;
import com.g10.locomobile.models.Train;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manage Line Activity
 * @author Group 10
 */
public class ManageLineActivity extends BaseActivity {

    // Initialize variables
    private ListView stopView;
    private Train selectedTrain;
    private Line currrentLine;
    private ArrayList<Stop> currentStopList;
    private ArrayList<String> stopNames;
    private Button addStopButton, saveButton;

    /**
     * On Create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_line);

        db = FirebaseFirestore.getInstance();

        final EditText taskEditText = new EditText(this);

        Intent intent = getIntent();

        selectedTrain = (Train) intent.getSerializableExtra("train");

        setTitle("Manage line of: " + selectedTrain.getname());

        stopView = (ListView) findViewById(R.id.listStops);
        addStopButton = findViewById(R.id.addStopButton);
        saveButton = findViewById(R.id.saveButton);

        currrentLine = selectedTrain.getline();

        currentStopList = currrentLine.getstops();

        stopNames = new ArrayList<>();

        for (Stop s : currentStopList) {
            stopNames.add(s.getName());
        }

        final StopListAdapter adapter = new StopListAdapter(stopNames, this);
        stopView.setAdapter(adapter);

        stopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Clicked on stop");
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Stop")
                .setMessage("Please enter the stop name")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText()).toUpperCase();

                        if (stopNames.contains(task)){
                            Toast.makeText(getApplicationContext(), "Stop already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentStopList.add(new Stop(task));

                        stopNames.add(task);

                        adapter.notifyDataSetChanged();

                        System.out.println("UPDATEEE" + selectedTrain.getline().getstops().toString());

                        taskEditText.setText("");
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();


        addStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("add stop button");
                dialog.show();

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(currentStopList.toString());

                Iterator i = currentStopList.iterator();
                while (i.hasNext()) {
                    if (!stopNames.contains(((Stop) i.next()).getName())) {
                        i.remove();
                    }
                }

                db.collection("trains").document(selectedTrain.getname())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });


                db.collection("trains").document(selectedTrain.getname())
                        .set(selectedTrain)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: ");
                                Toast.makeText(ManageLineActivity.this, "Success",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(ManageLineActivity.this, "Fail",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        });


    }

}
