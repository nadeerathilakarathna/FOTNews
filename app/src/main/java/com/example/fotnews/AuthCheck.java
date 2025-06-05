package com.example.fotnews;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class AuthCheck {
    public static void redirectLogin(Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Not logged in, go to login screen
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static void redirectFeed(Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();



        if (currentUser != null) {

            currentUser.getIdToken(true).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    String idToken = task2.getResult().getToken();
                    Log.d("FIREBASE_ID_TOKEN", "Token: " + idToken);
                } else {
                    Log.e("FIREBASE_ID_TOKEN", "Error getting token", task2.getException());
                }
            });

            String email = currentUser.getEmail();
            Toast.makeText(activity, "Logged in as: \n" + email, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, FeedActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}