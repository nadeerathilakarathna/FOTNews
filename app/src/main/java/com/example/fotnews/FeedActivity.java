package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
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
    private static final String TAG = "FOTNewsLog";
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

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        topAppBars.inflateMenu(R.menu.feed_top_menu);



        topAppBars.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(FeedActivity.this, DeveloperActivity.class);
            startActivity(intent);
            finish();
        });




        View rootView = findViewById(R.id.rootView);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsets.Type.systemBars()); // Status + nav bar

            // Apply top and bottom padding to leave space
            v.setPadding(0, bars.top, 0, 0);
            return insets;
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.menu_profile){
            Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
