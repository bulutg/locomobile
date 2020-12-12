package com.g10.locomobile.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.RegisterActivity;
import com.g10.locomobile.models.Line;
import com.g10.locomobile.models.Train;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TrainCreateActivity extends BaseActivity {
    protected Train train;
    private EditText editName, editSize, dateInput, hourInput;
    private String trainName, trainDate, trainHour;
    private int trainSize;
    private Button addButton;
    protected ArrayList<Train> tempTrains = new ArrayList<>();
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private int CalendarHour, CalendarMinute;
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_create);

        editName = (EditText) findViewById(R.id.trainName);
        editSize = (EditText) findViewById(R.id.size);

        addButton = (Button) findViewById(R.id.addbutton);
        dateInput = (EditText) findViewById(R.id.dateInput);
        hourInput = (EditText) findViewById(R.id.hourInput);

        mProgressBar = (ProgressBar) findViewById(R.id.creatingProcessBar);

        hideProgressBar();

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

        try {
            FileInputStream fi = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "/" + "myTrainList.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            try {
                for (; ; ) {
                    tempTrains.add((Train) oi.readObject());
                }
            } catch (EOFException e) {
                // End of stream
            }

            System.out.println("OLDU INS" + tempTrains.toString());

            oi.close();
            fi.close();

        } catch (Exception e) {
            System.out.println("EXCEPTIONN:" + e.toString());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressBar();


                trainName = editName.getText().toString().trim();
                trainDate = dateInput.getText().toString().trim();
                trainHour = hourInput.getText().toString().trim();
                trainSize = Integer.parseInt(editSize.getText().toString().trim());

                if (TextUtils.isEmpty(editSize.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Enter size!", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                    return;
                }

                if (trainSize > 5) {
                    Toast.makeText(getApplicationContext(), "Size is too high", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                    return;
                }

                train = new Train(trainName, trainSize, trainDate, trainHour);

                if (tempTrains.contains(train)) {
                    Toast.makeText(TrainCreateActivity.this, "Train already exists",
                            Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                } else {
                    setTrain(train);
                }

            }
        });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(TrainCreateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        hourInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = myCalendar.get(Calendar.MINUTE);

                timepickerdialog = new TimePickerDialog(TrainCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }

                                hourInput.setText(hourOfDay + ":" + minute + format);


                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();


            }
        });

    }

    protected void setTrain(Train train) {
        db = FirebaseFirestore.getInstance();

        db.collection("trains").document(train.getname())
                .set(train)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                        Toast.makeText(TrainCreateActivity.this, "Success",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(TrainCreateActivity.this, AdminActivity.class);
                        startActivity(intent);

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(TrainCreateActivity.this, "Fail",
                                Toast.LENGTH_SHORT).show();
                    }

                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TrainCreateActivity.this, AdminActivity.class);
        startActivity(intent);

        finish();
    }
}
