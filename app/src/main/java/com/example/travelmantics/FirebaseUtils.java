package com.example.travelmantics;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseUtils {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;

    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    private static FirebaseUtils firebaseUtil;
    public static ArrayList<TravelDeal> mDeals;

    private static ListActivity caller;

    private static final int RC_SIGN_IN = 123;

    private FirebaseUtils(){}

    public static void openFbReference(String ref, final ListActivity callerActivity){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtils(); //Class wasn't initialized, initialize now.
            mFirebaseDatabase = FirebaseDatabase.getInstance(); //Only getInstance the first time.
            
            // To be able to use the signIn()/startActivityForResult() method,
            // you need an initialize a caller Activity here:
            caller = callerActivity;
            
            //Initialing the Firebase Authenticator:
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()==null){
                        FirebaseUtils.signIn();
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
                }
            };
        }
        mDeals = new ArrayList<>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        
        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void logout(final Context context){
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "See you soon.", Toast.LENGTH_SHORT).show();
                        FirebaseUtils.attachListener();
                    }
                });
    }

    public static void attachListener(){
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
