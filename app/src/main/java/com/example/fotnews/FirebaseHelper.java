package com.example.fotnews;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.textfield.TextInputEditText;

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





    public static void getUserDetails(Activity activity, TextView nameView, TextView emailView, TextInputEditText edit_name, TextInputEditText edit_email, TextInputEditText edit_oldpassword, TextInputEditText edit_newpassword, TextInputEditText edit_confirmpassword) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(uid);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);


                    nameView.setText(name);
                    emailView.setText(email);
                    edit_name.setText(name);
                    edit_email.setText(email);

                } else {
                    Toast.makeText(activity, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void updateUserProfile(Activity activity, String newUsername, String oldPassword, String newPassword, Runnable onSuccess) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        String email = user.getEmail();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        userRef.child("name").get().addOnSuccessListener(dataSnapshot -> {
            String old_username = dataSnapshot.getValue(String.class);

            boolean needsUsernameUpdate = !old_username.equals(newUsername);
            boolean needsPasswordUpdate = !oldPassword.isEmpty() && !newPassword.isEmpty();

            if (needsUsernameUpdate) {
                userRef.child("name").setValue(newUsername)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(activity, "Username updated successfully", Toast.LENGTH_SHORT).show();
                            if (!needsPasswordUpdate) {
                                onSuccess.run(); // Only username changed
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(activity, "Username update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            if (!oldPassword.isEmpty()) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                user.reauthenticate(credential)
                        .addOnSuccessListener(authResult -> {
                            if (!newPassword.isEmpty()) {
                                user.updatePassword(newPassword)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(activity, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            if (!needsUsernameUpdate) {
                                                onSuccess.run(); // Only password changed
                                            } else if (needsUsernameUpdate) {
                                                onSuccess.run(); // Both done
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(activity, "Password update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            // If no username or password update needed
            if (!needsUsernameUpdate && !needsPasswordUpdate) {
                onSuccess.run();
            }

        }).addOnFailureListener(e -> {
            Toast.makeText(activity, "Failed to read username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


}
