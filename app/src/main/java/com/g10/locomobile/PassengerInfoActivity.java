package com.g10.locomobile;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.g10.locomobile.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class PassengerInfoActivity extends BaseActivity {

    private TextView nameView, idView, bdayView, typeView, sexView, walletView;

    public String sexOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_info);

        setTitle("Profile");

        nameView = (TextView) findViewById(R.id.name);
        idView = (TextView) findViewById(R.id.identification);
        bdayView = (TextView) findViewById(R.id.birthday);
        typeView = (TextView) findViewById(R.id.type);
        sexView = (TextView) findViewById(R.id.sex);
        walletView = (TextView) findViewById(R.id.wallet);

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

                        nameView.setText("Name: " + userName);
                        idView.setText("ID: " + userID);
                        bdayView.setText("Birthday: " + userBirthday);
                        typeView.setText("Type: " + userType);
                        sexView.setText("Sex: " + userSex);
                        walletView.setText("Wallet: " + userWallet);
                        sexOfUser = userSex;


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public String getSex() {
        return sexOfUser;
    }
}
