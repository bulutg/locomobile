package com.g10.locomobile.user;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.Seat;
import com.g10.locomobile.models.Train;
import com.g10.locomobile.models.User;
import com.g10.locomobile.models.Wagon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class TrainDetailsActivity extends BaseActivity {

    private TextView trainNameView, sizeView, occupiedSeatsView, occupancyRateView, seatCountView, dateView,seatNumberView, wagonNumberView;
    private Train selectedTrain;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private User userLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);

        setTitle("Train Details");

        Intent intent = getIntent();

        selectedTrain = (Train) intent.getSerializableExtra("train");

        trainNameView = (TextView) findViewById(R.id.trainName);
        sizeView = (TextView) findViewById(R.id.trainSize);
        occupiedSeatsView = (TextView) findViewById(R.id.occupiedSeats);
        occupancyRateView = (TextView) findViewById(R.id.occupancyRate);
        seatCountView = (TextView) findViewById(R.id.seatCount);
        dateView = (TextView) findViewById(R.id.trainDate);

        wagonNumberView = (TextView) findViewById(R.id.wagonNumber);
        seatNumberView = (TextView) findViewById(R.id.seatNumber);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        uid = user.getUid();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final User userLocal = document.toObject(User.class);

                        String userName = userLocal.getName();
                        Long userID = userLocal.getid();
                        String userBirthday = userLocal.getAge();
                        String userType = userLocal.getType();
                        String userSex = userLocal.getSex();
                        double userWallet = userLocal.getWallet();

                        Wagon wagon = selectedTrain.isUserInTrain(userLocal);
                        Seat seat = wagon.isUserInWagon(userLocal);

                        trainNameView.setText("Train Name: " + selectedTrain.getname());
                        sizeView.setText("Wagon Count: " + selectedTrain.getsize());
                        occupiedSeatsView.setText("Occupied Seat Number: " + selectedTrain.getOccupiedSeats());
                        occupancyRateView.setText("Occupancy Rate: %" + decimalFormat.format( selectedTrain.getOccupancyRate()));
                        seatCountView.setText("Seat Count: " + selectedTrain.getSeatCount());
                        dateView.setText("Date: " + selectedTrain.getdate() + " - " + selectedTrain.gethour());
                        wagonNumberView.setText("Wagon Number: " + wagon.toString());
                        seatNumberView.setText(seat.toString());


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}

