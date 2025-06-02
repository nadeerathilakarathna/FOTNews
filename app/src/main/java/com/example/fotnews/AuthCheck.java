package com.example.fotnews;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class AuthCheck {
    public static void redirectLogin(Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // 🚫 Not logged in, go to login screen
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
            String email = currentUser.getEmail();
            Toast.makeText(activity, "Logged in as: " + email, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, FeedActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}