package com.g10.locomobile.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.HomeActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.Seat;
import com.g10.locomobile.models.Stop;
import com.g10.locomobile.models.Train;
import com.g10.locomobile.models.TrainSystem;
import com.g10.locomobile.models.User;
import com.g10.locomobile.models.Wagon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SeatActivity extends BaseActivity {


    protected ArrayList<ImageButton> buttons;
    ArrayList<ImageButton> selectedButtons = new ArrayList<ImageButton>();
    ArrayList<Seat> selectedSeats;
    protected Wagon wagon;
    //    private Button buyButton, clearButton;
    protected Train train;

    User userLocal;
    protected Drawable emptySeat;
    protected Drawable blueSeat;
    protected Drawable pinkSeat;
    private Spinner spinner;


    /**
     * Draws and functions main screen.
     *
     * @param savedInstanceState bundled state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        Intent intent = getIntent();

        String trainName = intent.getStringExtra("trainName");
        final String departure = getIntent().getStringExtra("KEY_departure");
        final String destination = getIntent().getStringExtra("KEY_destination");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();

        userLocal = TrainSystem.convertUser(user, auth, db);
        emptySeat = getResources().getDrawable(R.drawable.empty_seat); //Empty Seat
        blueSeat = getResources().getDrawable(R.drawable.blue_seat); // Blue Seat
        pinkSeat = getResources().getDrawable(R.drawable.pink_seat); // Pink Seat

        buttons = new ArrayList<ImageButton>();

        //buyButton = (Button) findViewById(R.id.buyButton);
        //clearButton = (Button) findViewById(R.id.clearButton);
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding imagebuttons to the arraylist.
        buttons.add((ImageButton) findViewById(R.id.imageButton1));
        buttons.add((ImageButton) findViewById(R.id.imageButton2));
        buttons.add((ImageButton) findViewById(R.id.imageButton3));
        buttons.add((ImageButton) findViewById(R.id.imageButton4));
        buttons.add((ImageButton) findViewById(R.id.imageButton5));
        buttons.add((ImageButton) findViewById(R.id.imageButton6));
        buttons.add((ImageButton) findViewById(R.id.imageButton7));
        buttons.add((ImageButton) findViewById(R.id.imageButton8));
        buttons.add((ImageButton) findViewById(R.id.imageButton9));
        buttons.add((ImageButton) findViewById(R.id.imageButton10));
        buttons.add((ImageButton) findViewById(R.id.imageButton11));
        buttons.add((ImageButton) findViewById(R.id.imageButton12));
        buttons.add((ImageButton) findViewById(R.id.imageButton13));
        buttons.add((ImageButton) findViewById(R.id.imageButton14));
        buttons.add((ImageButton) findViewById(R.id.imageButton15));
        buttons.add((ImageButton) findViewById(R.id.imageButton16));
        buttons.add((ImageButton) findViewById(R.id.imageButton17));
        buttons.add((ImageButton) findViewById(R.id.imageButton18));
        buttons.add((ImageButton) findViewById(R.id.imageButton19));
        buttons.add((ImageButton) findViewById(R.id.imageButton20));
        buttons.add((ImageButton) findViewById(R.id.imageButton21));
        buttons.add((ImageButton) findViewById(R.id.imageButton22));
        buttons.add((ImageButton) findViewById(R.id.imageButton23));
        buttons.add((ImageButton) findViewById(R.id.imageButton24));
        buttons.add((ImageButton) findViewById(R.id.imageButton25));
        buttons.add((ImageButton) findViewById(R.id.imageButton26));
        buttons.add((ImageButton) findViewById(R.id.imageButton27));
        buttons.add((ImageButton) findViewById(R.id.imageButton28));
        buttons.add((ImageButton) findViewById(R.id.imageButton29));
        buttons.add((ImageButton) findViewById(R.id.imageButton30));
        buttons.add((ImageButton) findViewById(R.id.imageButton31));
        buttons.add((ImageButton) findViewById(R.id.imageButton32));
        buttons.add((ImageButton) findViewById(R.id.imageButton33));
        buttons.add((ImageButton) findViewById(R.id.imageButton34));
        buttons.add((ImageButton) findViewById(R.id.imageButton35));
        buttons.add((ImageButton) findViewById(R.id.imageButton36));

        final ArrayList<Wagon> array = new ArrayList<Wagon>();
        final ArrayList<String> arrayList = new ArrayList<String>();

        System.out.println(trainName + ".");


        arrayList.add("Select wagon");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeatActivity.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("trains").document(trainName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Train train = documentSnapshot.toObject(Train.class);
                System.out.println("XXXXX" + train.toString());
                for (int i = 1; i <= train.getwagons().size(); i++) {
                    arrayList.add("Wagon " + i);
                }

                array.addAll(train.getwagons()); // Adding wagons to the combobox array

                wagon = train.getwagons().get(0);
                refreshButton();
                selectedButtons.clear();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedWagon = (String) parent.getSelectedItem();

                        if (selectedWagon.equals("Select wagon")) {
                            Toast.makeText(SeatActivity.this, "Please select wagon first",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            wagon = train.getwagons().get(parent.getSelectedItemPosition() - 1);
                            refreshButton();
                            System.out.println(parent.getSelectedItemPosition());
                            selectedButtons.clear();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final View.OnClickListener listener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final ArrayList<String> tmp = new ArrayList<>();

                        final ImageButton seat = (ImageButton) findViewById(v.getId());
                        seat.setClickable(true);

                        if (!(seat.getDrawable() == emptySeat)) return;

                        FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                        final double userWallet = userLocal.getWallet();

                                        if (userSex.equalsIgnoreCase("Male"))
                                            seat.setImageDrawable(blueSeat);
                                        else
                                            seat.setImageDrawable(pinkSeat);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(SeatActivity.this);

                                        builder.setTitle("Buy seat for " + train.getname());
                                        builder.setMessage("Your balance is: "
                                                + userWallet
                                                + "\nAre you sure?");

                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                Seat localSeat = wagon.getseats().get(buttons.indexOf(seat));
                                                localSeat.setowner(userLocal);
                                                selectedButtons.add(seat);

                                                userLocal.setWallet(userWallet - DEFAULT_PRICE);

                                                ArrayList<Stop> stops = train.getline().getstops();
                                                Stop from = new Stop();
                                                Stop to = new Stop();

                                                for (Stop s : stops) {
                                                    if (s.getName().equalsIgnoreCase(departure)) {
                                                        from = s;
                                                    }
                                                    if (s.getName().equalsIgnoreCase(destination)) {
                                                        to = s;
                                                    }
                                                }


                                                train.getline();

//                                                Ticket ticket = new Ticket(train.getline(), localSeat, from, to, DEFAULT_PRICE);
                                                train.calculateOccupiedSeats();
                                                train.calculateOccupancyRate();

                                                db = FirebaseFirestore.getInstance();

                                                db.collection("trains").document(train.getname())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                                                db.collection("trains").document(train.getname())
                                                                        .set(train)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d(TAG, "DocumentSnapshot successfully written!");

                                                                                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error writing document", e);
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error deleting document", e);
                                                            }
                                                        });





                                            }
                                        });

                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                selectedButtons.clear();
                                                refreshButton();

                                            }
                                        });


                                        final AlertDialog alert = builder.create();
                                        alert.show();


                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });


                    }
                };

                for (ImageButton button : buttons) {
                    button.setOnClickListener(listener);
                }


                System.out.println(spinner.getSelectedItem());


//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        System.out.println(findSelectedSeatButtons());
//                    }
//                });
//                clearButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        selectedButtons.clear();
//                        refreshButton();
//                    }
//                });

            }
        });


    }

    /**
     * Refreshes buttons according to the selected wagon.
     */
    private void refreshButton() {
        ArrayList<Seat> seats = wagon.getseats();
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seat.getowner() == null) {
                buttons.get(i).setClickable(true);
                buttons.get(i).setImageDrawable(emptySeat);
            } else {
                buttons.get(i).setClickable(false);
                if (seat.getowner().getSex().equals("Male")) {
                    buttons.get(i).setImageDrawable(blueSeat);

                } else {
                    buttons.get(i).setImageDrawable(pinkSeat);
                }
            }
        }
    }

    /**
     * Return selected seats with converting selected buttons to the seats.
     *
     * @return Selected Seats
     */
    private ArrayList<Seat> findSelectedSeatButtons() {
        ArrayList<Seat> selectedSeats = new ArrayList<Seat>();

        for (ImageButton button : selectedButtons) {
            selectedSeats.add(wagon.getseats().get(buttons.indexOf(button)));
        }
        return selectedSeats;
    }
}
