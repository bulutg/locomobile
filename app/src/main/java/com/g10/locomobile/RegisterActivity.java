package com.g10.locomobile;

import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.g10.locomobile.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends BaseActivity {

    private FirebaseAuth auth;
    private User user;
    private Button btnRegister;
    private EditText emailInput, passwordInput, nameInput, surnameInput, idInput, birthdayInput, typeInput, sexInput;
    private RadioGroup sexSelection;
    private String selectedSex;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressBar = findViewById(R.id.registeringProcessBar);

        auth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        nameInput = (EditText) findViewById(R.id.firstname);
        surnameInput = (EditText) findViewById(R.id.surname);
        idInput = (EditText) findViewById(R.id.identification);
        birthdayInput = (EditText) findViewById(R.id.birthday);
//        typeInput = (EditText) findViewById(R.id.type);
//        sexInput = (EditText) findViewById(R.id.sex);


        btnRegister = (Button) findViewById(R.id.register);
        btnLogin = (TextView) findViewById(R.id.login);

        sexSelection = (RadioGroup) findViewById(R.id.radioSex);

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

                birthdayInput.setText(sdf.format(myCalendar.getTime()));
            }

        };

        birthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                final String firstname = nameInput.getText().toString().trim();
                final String surname = surnameInput.getText().toString().trim();
                final String identification = idInput.getText().toString().trim();
                final String birthday = birthdayInput.getText().toString().trim();
                final int sex = sexSelection.getCheckedRadioButtonId();
                final String type = "default";

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //
                if (!User.isValidTCKN(Long.parseLong(identification))) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid T.C. ID number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (findViewById(sexSelection.getCheckedRadioButtonId()) == null) {
                    Toast.makeText(getApplicationContext(), "Please select your sex", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedSex = ((RadioButton) findViewById(sex)).getText().toString();

                showProgressBar();

                btnRegister.setClickable(false);
                btnRegister.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                btnRegister.setClickable(false);
                btnRegister.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(RegisterActivity.this, "Registered successfully",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUI(user);

                                    String providerID = user.getUid();
                                    Log.d(TAG, "ProvID: " + providerID);
                                    setUser(email, firstname, surname, identification, birthday, selectedSex, type, providerID, 100);

                                    Intent goToNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(goToNextActivity);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                btnLogin.setClickable(true);
                                btnLogin.getBackground().setColorFilter(null);
                                btnRegister.setClickable(true);
                                btnRegister.getBackground().setColorFilter(null);
                            }
                        });
            }
        });

        // Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userExist()) {

                    Toast.makeText(RegisterActivity.this, "Already logged in",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goToNextActivity);
                }
            }
        });
    }


    protected void setUser(String email, String name, String lastName, String ID, String birth, String sex, String type, String provide, double wallet) {
//        Map<String, Object> user = new HashMap<>();
//        user.put("e-mail", email);
//        user.put("name", name);
//        user.put("surname", lastName);
//        user.put("ID", ID);
//        user.put("birthday", birth);
//        user.put("sex", sex);
//        user.put("type", type);
//        user.put("wallet", wallet);

        User newUser = new User(name, birth ,Long.parseLong(ID),sex,type,wallet); // buraya eklenecek

        FirebaseFirestore.getInstance().collection("users").document(provide)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(RegisterActivity.this, "Fail",
                                Toast.LENGTH_SHORT).show();
                    }

                });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

}