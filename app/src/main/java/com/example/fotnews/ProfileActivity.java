package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }
}
