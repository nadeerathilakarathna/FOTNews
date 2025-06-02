package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DeveloperActivity extends AppCompatActivity {
    private static final String TAG = "FOTNewsLog";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectLogin(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        setContentView(R.layout.activity_developer);
        MaterialToolbar devAppBar = findViewById(R.id.devAppBar);
        setSupportActionBar(devAppBar);





        // Settings (left) click
        devAppBar.setNavigationOnClickListener(view -> {
            Log.i(TAG, "Navigation Bar clicked");
            Toast.makeText(DeveloperActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeveloperActivity.this, FeedActivity.class);
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
