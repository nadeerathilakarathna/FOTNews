package com.example.fotnews;

import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivity extends AppCompatActivity {

    private MaterialToolbar topAppBars;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }




        setContentView(R.layout.activity_newsfeed);


        topAppBars = findViewById(R.id.topAppBar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_sport) {
                Toast.makeText(this, "Sport clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_academic) {
                Toast.makeText(this, "Academic clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_event) {
                Toast.makeText(this, "Events clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SportFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_sport) {
                selectedFragment = new SportFragment();
            } else if (itemId == R.id.nav_academic) {
                selectedFragment = new AcademicFragment();
            } else if (itemId == R.id.nav_event) {
                selectedFragment = new EventsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Settings (left) click
        topAppBars.setNavigationOnClickListener(v ->
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
        );

        // Profile (right) click
        topAppBars.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.profile) {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);






        View rootView = findViewById(R.id.rootView);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsets.Type.systemBars()); // Status + nav bar

            // Apply top and bottom padding to leave space
            v.setPadding(0, bars.top, 0, 0);
            return insets;
        });



        }







}
