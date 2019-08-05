package com.example.travelmantics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtils {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;

    private static FirebaseUtils firebaseUtil;
    public static ArrayList<TravelDeal> mDeals;

    private FirebaseUtils(){}

    public static void openFbReference(String ref){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtils(); //Class wasn't initialized, initialize now.
            mFirebaseDatabase = FirebaseDatabase.getInstance(); //Only getInstance the first time.
            mDeals = new ArrayList<>();
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }
}
