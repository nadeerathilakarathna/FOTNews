package com.example.fotnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FirebaseHelper {


    public static void registerUser(String name, String email, String password, Activity activity, Runnable progressbar_start_loader, Runnable progressbar_stop_loader) {

        progressbar_start_loader.run();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                String uid = mAuth.getCurrentUser().getUid();

                // 🔥 Create user object
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("name", name);
                userMap.put("email", email);
                userMap.put("uid", uid);

                // 🧠 Save to Firebase Realtime DB
                dbRef.child(uid).setValue(userMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show();
                        progressbar_stop_loader.run();
                        Intent intent = new Intent(activity, FeedActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        progressbar_stop_loader.run();
                        handleLoginError(task1.getException(), activity);
                    }
                });
            } else {
                progressbar_stop_loader.run();
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


    public static void getUserDetails(Activity activity, InterfaceHelper.OnUserDetailsReceived callback, Runnable onLoaded) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);


                    callback.onReceived(name, email);
                    onLoaded.run();


                } else {
                    Toast.makeText(activity, "User data not found", Toast.LENGTH_SHORT).show();
                    onLoaded.run();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void updateUserProfile(Activity activity, String newUsername, String oldPassword, String newPassword, InterfaceHelper.onUserDetailsUpdated callback, Runnable progressOnLoaded, Runnable onSuccess) {
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
                userRef.child("name").setValue(newUsername).addOnSuccessListener(unused -> {
                    Toast.makeText(activity, "Username updated successfully", Toast.LENGTH_SHORT).show();
                    if (!needsPasswordUpdate) {
                        onSuccess.run(); // Only username changed
                        progressOnLoaded.run();
                        callback.onUpdated();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(activity, "Username update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressOnLoaded.run();
                });
            }

            if (!oldPassword.isEmpty()) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                user.reauthenticate(credential).addOnSuccessListener(authResult -> {
                    if (!newPassword.isEmpty()) {
                        user.updatePassword(newPassword).addOnSuccessListener(unused -> {
                            Toast.makeText(activity, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            if (!needsUsernameUpdate) {
                                onSuccess.run(); // Only password changed
                                progressOnLoaded.run();
                                callback.onUpdated();
                            } else if (needsUsernameUpdate) {
                                onSuccess.run(); // Both done
                                progressOnLoaded.run();
                                callback.onUpdated();
                            }
                        }).addOnFailureListener(e -> {
                            progressOnLoaded.run();
                            Toast.makeText(activity, "Password update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }).addOnFailureListener(e -> {
                    progressOnLoaded.run();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            // If no username or password update needed
            if (!needsUsernameUpdate && !needsPasswordUpdate) {
                onSuccess.run();
                progressOnLoaded.run();
            }

        }).addOnFailureListener(e -> {
            progressOnLoaded.run();
            Toast.makeText(activity, "Failed to read username: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    public static void loadNews(int requiredCategory, List<NewsItem> targetList, Context context, NewsAdapter adapter, Runnable onLoaded) {
        Query newsRef = FirebaseDatabase.getInstance()
                .getReference("News")
                .orderByKey();


        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                targetList.clear();
                                for (DataSnapshot newsSnap : snapshot.getChildren()) {
                    try {
                        String title = newsSnap.child("title").getValue(String.class);
                        String content = newsSnap.child("content").getValue(String.class);
                        int category = newsSnap.child("category").getValue(Integer.class);
                        String image_location = newsSnap.child("image").getValue(String.class);
                        String timestamp = newsSnap.getKey();
                        int category_icon;


                        if (category == requiredCategory) {
                            if (category == 0) {
                                category_icon = R.drawable.ic_academic;
                            } else if (category == 1) {
                                category_icon = R.drawable.ic_events;
                            } else {
                                category_icon = R.drawable.ic_sports;
                            }
                            NewsItem item = new NewsItem(title, content, timestamp, category_icon, image_location);
                            targetList.add(item);
                        } else if (requiredCategory == 100) {

                            if (category == 0) {
                                category_icon = R.drawable.ic_academic;
                            } else if (category == 1) {
                                category_icon = R.drawable.ic_events;
                            } else {
                                category_icon = R.drawable.ic_sports;
                            }
                            NewsItem item = new NewsItem(title, content, timestamp, category_icon, image_location);
                            targetList.add(item);
                        }

                    } catch (Exception e) {
                        onLoaded.run();
                        e.printStackTrace();
                    }
                }
                Collections.reverse(targetList);
                adapter.notifyDataSetChanged();
                onLoaded.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                                onLoaded.run();
            }
        });
    }

}
