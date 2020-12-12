package com.g10.locomobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.g10.locomobile.models.Stop;
import com.g10.locomobile.models.Train;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class TrainSearchActivity extends BaseActivity {

    private Button searchButton;
    private EditText dateInput;
    private String selectedType;
    private ArrayList<Train> trains;
    private ArrayList<String> allStopNames1, allStopNames2;
    String departure;
    String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);

        setTitle("Train Search");

        trains = new ArrayList<>();
        allStopNames1 = new ArrayList<>();
        allStopNames2 = new ArrayList<>();

        mProgressBar = findViewById(R.id.searchingProcessBar);


        showProgressBar();

        FirebaseFirestore.getInstance().collection("trains")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Train t = document.toObject(Train.class);
                                trains.add(t);
                                if (t.getline().getstops() != null) {
                                    for (Stop s : t.getline().getstops()) {
                                        if (!allStopNames1.contains(s.getName())) {
                                            allStopNames1.add(s.getName());
                                            allStopNames2.add(s.getName());
                                        }
                                    }
                                }
                            }

                            System.out.println("CEKTI INS: " + trains.toString());
                            System.out.println(" INS: " + allStopNames1.toString());
                            hideProgressBar();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        allStopNames1.add(0,"From:");
        allStopNames2.add(0,"To:");

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, allStopNames1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, allStopNames2);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

//        spinner1.setSelection("From");
//        spinner2.setSelection(spinpos);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departure = (String) parent.getSelectedItem();

                Toast.makeText(TrainSearchActivity.this, "Selection :" + parent.getSelectedItem(),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destination = (String) parent.getSelectedItem();
                Toast.makeText(TrainSearchActivity.this, "Selection :" + parent.getSelectedItem(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        departureInput = (EditText) findViewById(R.id.departure);
//        destinationInput = (EditText) findViewById(R.id.destination);
        dateInput = (EditText) findViewById(R.id.date);

        searchButton = (Button) findViewById(R.id.searchButton);

        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd.MM.yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateInput.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(TrainSearchActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressBar();

                if (departure.equals("From:") || destination.equals("To:")){
                    Toast.makeText(TrainSearchActivity.this, "Please select departure and destination",
                            Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                    return;
                }

                updateDataSet();

                for (Train t : upToDateTrainData) {
                    System.out.println("XXAAA" + t.toString());
                }


                final String date = dateInput.getText().toString().trim();

                searchButton.setClickable(false);
                searchButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                Intent goToNextActivity = new Intent(getApplicationContext(), SearchResultsActivity.class);

                goToNextActivity.putExtra("KEY_departure", departure);
                goToNextActivity.putExtra("KEY_destination", destination);
                goToNextActivity.putExtra("KEY_date", date);

                startActivity(goToNextActivity);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchButton.setClickable(true);
        searchButton.getBackground().setColorFilter(null);
    }
}
