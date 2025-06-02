package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void logoutUser(LinearLayout alert_background,CoordinatorLayout alert_logout) {
        FirebaseAuth.getInstance().signOut();
        alert_background.setVisibility(View.GONE);
        alert_logout.setVisibility(View.GONE);
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectLogin(this);
        setContentView(R.layout.activity_profile);


        MaterialToolbar profileAppBar = findViewById(R.id.profileappbar);
        setSupportActionBar(profileAppBar);

        profileAppBar.setNavigationOnClickListener(view -> {
            Toast.makeText(ProfileActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        });


        View rootView = findViewById(R.id.rootView);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsets.Type.systemBars()); // Status + nav bar

            // Apply top and bottom padding to leave space
            v.setPadding(0, bars.top, 0, bars.bottom);
            return insets;
        });

        MaterialButton btn_edit_profile = findViewById(R.id.btn_edit_profile);
        MaterialButton btn_logout = findViewById(R.id.btn_logout);
        LinearLayout alert_background = findViewById(R.id.userview_edit);
        CoordinatorLayout alert_edit_profile = findViewById(R.id.userview_box_edit);
        CoordinatorLayout alert_logout = findViewById(R.id.userview_box_logout);
        ImageButton btn_close_edit_profile = findViewById(R.id.close_edit_profile);
        ImageButton btn_close_logout = findViewById(R.id.close_logout);
        MaterialButton btn_edit_profile_save = findViewById(R.id.btn_edit_save);
        MaterialButton btn_edit_profile_cancel = findViewById(R.id.btn_edit_cancel);
        MaterialButton btn_logout_yes = findViewById(R.id.btn_logout_yes);
        MaterialButton btn_logout_no = findViewById(R.id.btn_logout_no);



        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.VISIBLE);
                alert_edit_profile.setVisibility(View.VISIBLE);
            }
        });

        btn_close_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.GONE);
                alert_edit_profile.setVisibility(View.GONE);
            }
        });

        btn_edit_profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                alert_background.setVisibility(View.GONE);
                alert_edit_profile.setVisibility(View.GONE);
            }
        });

        btn_edit_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.GONE);
                alert_edit_profile.setVisibility(View.GONE);
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.VISIBLE);
                alert_logout.setVisibility(View.VISIBLE);
            }
        });

        btn_close_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.GONE);
                alert_logout.setVisibility(View.GONE);
            }
        });

        btn_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser(alert_background,alert_logout);
            }
        });

        btn_logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_background.setVisibility(View.GONE);
                alert_logout.setVisibility(View.GONE);
            }
        });







    }
}
