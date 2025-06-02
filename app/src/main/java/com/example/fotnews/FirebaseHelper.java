package com.example.fotnews;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseHelper {

    public static void registerUser(String name, String email, String password, Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // 🔥 Create user object
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("uid", uid);

                        // 🧠 Save to Firebase Realtime DB
                        dbRef.child(uid).setValue(userMap)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity, FeedActivity.class);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    } else {
                                        handleLoginError(task1.getException(), activity);
                                    }
                                });
                    } else {
                        handleLoginError(task.getException(), activity);
                    }
                });
    }

    public static void handleLoginError(Exception e, Context context) {
        String errorMsg = "Something went wrong";

        if (e instanceof FirebaseAuthWeakPasswordException) {
            errorMsg = "Weak password, try something stronger.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            errorMsg = "Invalid email format.";
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            errorMsg = "This email is already registered.";
        } else if (e != null) {
            errorMsg = e.getMessage();
        }

        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
    }
}
