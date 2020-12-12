package com.g10.locomobile.ui.main;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.g10.locomobile.HomeActivity;
import com.g10.locomobile.LoginActivity;
import com.g10.locomobile.R;
import com.g10.locomobile.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DiscountManagementFragment extends PlaceholderFragment {

    private TextView nameView, idView, bdayView, typeView, sexView, walletView;
    private Button logOutButton;

    public static DiscountManagementFragment newInstance(int index) {
        DiscountManagementFragment activity = new DiscountManagementFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        activity.setArguments(bundle);

        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance();

        View view = getView();


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discount_management, container, false);

        nameView = (TextView) root.findViewById(R.id.adminName);
        idView = (TextView) root.findViewById(R.id.identification);
        bdayView = (TextView) root.findViewById(R.id.birthday);
        typeView = (TextView) root.findViewById(R.id.type);
        sexView = (TextView) root.findViewById(R.id.sex);
        walletView = (TextView) root.findViewById(R.id.wallet);
        logOutButton= (Button) root.findViewById(R.id.logoutButton);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String uid = user.getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);

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


                    }
                }
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

}
