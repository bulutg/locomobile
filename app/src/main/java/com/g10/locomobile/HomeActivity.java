package com.g10.locomobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.g10.locomobile.admin.AdminActivity;
import com.g10.locomobile.models.User;
import com.g10.locomobile.user.MyTicketsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;

public class HomeActivity extends BaseActivity {

    private Button btnRegister, btnMyTickets;
    private Button btnProfile, btnSearch, btnVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("LocoMobile Main Menu");

        btnProfile = (Button) findViewById(R.id.profile);
        btnRegister = (Button) findViewById(R.id.signup);
        btnMyTickets = (Button) findViewById(R.id.mytickets);
        btnSearch = (Button) findViewById(R.id.trainSearch);
        btnVariable = (Button) findViewById(R.id.variableButton);

        btnRegister.setVisibility(View.INVISIBLE);
        btnProfile.setVisibility(View.INVISIBLE);
        btnVariable.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        if (!userExist()) {

            btnRegister.setVisibility(View.VISIBLE);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(goToNextActivity);
                    finish();
                }
            });

            btnVariable.setText("Sign in");

            btnVariable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goToNextActivity);
                    finish();

                }
            });
            btnVariable.setVisibility(View.VISIBLE);
        } else {

            user = auth.getCurrentUser();
            String info = user.getUid();
            getUserInfo(info);

            btnVariable.setText("Log out");

            btnMyTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), MyTicketsActivity.class);
                    startActivity(goToNextActivity);
                }
            });

            btnVariable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            });

            db = FirebaseFirestore.getInstance();

            uid = user.getUid();

            DocumentReference docRef = db.collection("users").document(uid);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            final User userLocal = document.toObject(User.class);

                            if(userLocal.getType().equalsIgnoreCase("admin")) {

                                btnProfile.setVisibility(View.VISIBLE);

                                btnProfile.setText("Admin Panel");

                                btnProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent goToNextActivity = new Intent(getApplicationContext(), AdminActivity.class);
                                        startActivity(goToNextActivity);
                                        finish();
                                    }
                                });
                            }
                            else{
                                btnVariable.setVisibility(View.VISIBLE);
                                btnProfile.setVisibility(View.VISIBLE);

                                btnProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (auth.getCurrentUser() != null) {
                                            Intent intent = new Intent(getBaseContext(), PassengerInfoActivity.class);
                                            intent.putExtra("name", "Bulut");
                                            intent.putExtra("surname", "Gozubuyuk");
                                            intent.putExtra("id", "11111111111");
                                            intent.putExtra("birthday", "27.10.1998");
                                            intent.putExtra("type", "Student");
                                            intent.putExtra("sex", "Male");

                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), TrainSearchActivity.class);
                startActivity(goToNextActivity);
            }
        });


    }

    @Override
    protected void getUserInfo(String i) {
        db = FirebaseFirestore.getInstance();
        DocumentReference info = db.collection("users").document(i);
        info.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
