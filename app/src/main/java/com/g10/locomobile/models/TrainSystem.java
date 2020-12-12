
package com.g10.locomobile.models;

import android.util.Log;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * This class will have static methods that helps view classes.
 *
 */
public class TrainSystem {


    public ArrayList<discountCode> discountCodes;
    public static User userLocal;
    public ArrayList<Train> trains;
    public ArrayList<Line> lines;
    /**
     * Converts birth date to age
     * @param birth birth date.
     * @return age.
     */
    public static int birthToYear(String birth){

        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        int currentTime = Integer.parseInt(year.format(new Date()));

        return currentTime - Integer.parseInt(birth.substring(birth.length()-4));

    }

//    public static String remaingTime(Stop stop){
//        LocalTime time = LocalTime.of(stop.getTime().getHour(),stop.getTime().getMinute(),stop.getTime().getSecond());
//        long hour = LocalTime.now().getHour();
//        long minute = LocalTime.now().getMinute();
//        long second = LocalTime.now().getSecond();
//        if(time.compareTo(LocalTime.now()) < 0){
//            return "Already passed";
//        }
//        return "" + time.minusHours(hour).minusMinutes(minute).minusSeconds(second);
//    }

    /**
     * Checks the discount code.
     * @param code Discount code.
     * @return discount rate.
     */
    public double checkCode(String code) {

        for (discountCode codes : discountCodes) {
            if (codes.getDiscountCode().equals(code))
                return codes.getDiscountRate();
        }
        return 1; //Multiplier 0 for no discount.
    }

    //    public Train checkTrain( Seat seat ){
//           }
//    public Wagon checkWagon(Seat seat){
//        for ()
//    }

    /**
     * Adds new train.
     * @param train train
     */
    public void addTrain(Train train) {
        trains.add(train);
    }

    /**
     * Removes train from list
     * @param train train
     */
    public void removeTrain(Train train) {
        trains.remove(train);
    }

    /**
     * Adds new line
     * @param line line
     */
    public void addLine(Line line) {
        lines.add(line);
    }

    /**
     *Removes line from list
     * @param line line.
     */
    public void removeLine(Line line) {
        lines.remove(line);
    }

    /**
     * Converts database user to local user object.
     * @param user firebase user
     * @param auth auth
     * @param db database
     * @return Local User instance
     */
    public static User convertUser(FirebaseUser user, FirebaseAuth auth, FirebaseFirestore db) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User userLocal = document.toObject(User.class);

                        String userName = userLocal.getName();
                        Long userID = userLocal.getid();
                        String userBirthday = userLocal.getAge();
                        String userType = userLocal.getType();
                        String userSex = userLocal.getSex();

                        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                        int currentTime = Integer.parseInt(year.format(new Date()));

                        String sex;
                        String userTypeModel;

                        int userAge = birthToYear(userBirthday);

                        if (userSex.equalsIgnoreCase("Male"))
                            sex = "Male";
                        else
                            sex = "Female";

                        if (userType.equalsIgnoreCase("Default"))
                            userTypeModel = "default";
                        else if (userType.equalsIgnoreCase("Student") || userType.equalsIgnoreCase("Teacher")|| userType.equalsIgnoreCase("education"))
                            userTypeModel = "education";
                        else
                            userTypeModel = "disabled";

                    } else {
                        Log.d("Debug", "No such document");
                    }
                } else {
                    Log.d("Debug", "get failed with ", task.getException());
                }

            }
        });
        return userLocal;
    }

    /**
     * Searchs train according to given parameters
     * @param start Start stop
     * @param destination Stop stop
     * @param trains train list
     * @param date date
     * @return found trains
     */
    public static ArrayList<Train> searchTrain(Stop start, Stop destination, ArrayList<Train> trains, String date) {
        ArrayList<Train> foundTrains = new ArrayList<Train>();

        for (Train train : trains) {
            if (train.hasStop(start) && train.hasStop(destination)) {
                if (train.hasCorrectOrder(start, destination) && train.getdate().equals(date))
                    foundTrains.add(train);
            }
        }
        return foundTrains;
    }

    /**
     * Searchs train according to given parameters
     * @param start Start string
     * @param destination Stop string
     * @param trains train list
     * @param date date
     * @return found trains
     */
    public static ArrayList<Train> searchTrain(String start, String destination, ArrayList<Train> trains, String date) {
        ArrayList<Train> foundTrains = new ArrayList<Train>();

        for (Train train : trains) {
            Stop startStop = train.getline().findStop(start);
            Stop destinationStop = train.getline().findStop(destination);
            if (startStop != null && destinationStop != null) {
                if (train.hasStop(startStop) && train.hasStop(destinationStop)) {
                    if (train.hasCorrectOrder(startStop, destinationStop) && train.getdate().equals(date))
                        foundTrains.add(train);
                }
            }
        }
        return foundTrains;
    }





    public ArrayList<discountCode> getDiscountCodes() {
        return discountCodes;
    }
}
