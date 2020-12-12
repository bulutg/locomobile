package com.g10.locomobile.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.Train;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class TrainInfoActivity extends BaseActivity {

    private TextView trainNameView, sizeView, occupiedSeatsView, occupancyRateView, seatCountView, dateView;
    private Button deleteTrain, manageLine;
    private Train selectedTrain;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_info);

        setTitle("Train Information");

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        selectedTrain = (Train) intent.getSerializableExtra("train");
        trainNameView = (TextView) findViewById(R.id.trainName);
        sizeView = (TextView) findViewById(R.id.trainSize);
        occupiedSeatsView = (TextView) findViewById(R.id.occupiedSeats);
        occupancyRateView = (TextView) findViewById(R.id.occupancyRate);
        seatCountView = (TextView) findViewById(R.id.seatCount);
        dateView = (TextView) findViewById(R.id.trainDate);

        trainNameView.setText("Train Name: " + selectedTrain.getname());
        sizeView.setText("Train Size: " + selectedTrain.getsize());
        occupiedSeatsView.setText("Occupied Seat Number: " + selectedTrain.getOccupiedSeats());
        occupancyRateView.setText("Occupancy Rate: %" + decimalFormat.format(selectedTrain.getOccupancyRate()));
        seatCountView.setText("Seat Count: " + selectedTrain.getSeatCount());
        dateView.setText("Date: " + selectedTrain.getdate() + " - " + selectedTrain.gethour());

        deleteTrain = (Button) findViewById(R.id.deleteTrain);
        manageLine = (Button) findViewById(R.id.manageLineButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete train: " + selectedTrain.getname());
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
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
                Intent intent = new Intent(getBaseContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        deleteTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });

        manageLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ManageLineActivity.class);
                intent.putExtra("train", selectedTrain);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TrainInfoActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}

