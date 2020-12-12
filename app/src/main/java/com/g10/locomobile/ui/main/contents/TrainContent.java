package com.g10.locomobile.ui.main.contents;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.g10.locomobile.models.Train;
import com.g10.locomobile.ui.main.TrainManagementFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainContent extends Content {
    static FirebaseFirestore store;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Train> TRAINS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, Train> ITEM_MAP = new HashMap<>();

    private static final int COUNT = TRAINS.size();

    static {
        store.getInstance().collection("trains")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            try {

                                File file = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS), "/" + "myTrainList.txt");

                                FileOutputStream f = new FileOutputStream(file);
                                ObjectOutputStream o = new ObjectOutputStream(f);


                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    Train currentTrain = document.toObject(Train.class);

                                    o.writeObject(currentTrain);
                                    TRAINS.add(currentTrain);
                                    ITEM_MAP.put(0, currentTrain);
                                    TrainManagementFragment.adapter.notifyDataSetChanged();

                                }
                                o.close();
                                f.close();
                            } catch (Exception e) {
                                System.out.println("CONTENT EXCEPTION" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


}
