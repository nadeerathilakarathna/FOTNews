package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivity extends AppCompatActivity {
    private MaterialToolbar topAppBars;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectLogin(this);


        UiHelper.setStatusBarandNavigationBarColor(FeedActivity.this, R.color.color_primary, R.color.color_primary);


        setContentView(R.layout.activity_newsfeed);
        topAppBars = findViewById(R.id.topAppBar);

        UiHelper.setStatusBarPadding(FeedActivity.this, topAppBars);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_all) {
                return true;
            } else if (id == R.id.nav_sport) {
                return true;
            } else if (id == R.id.nav_academic) {
                return true;
            } else if (id == R.id.nav_event) {
                return true;
            }
            return false;
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_sport) {
                selectedFragment = new SportFragment();
            } else if (itemId == R.id.nav_academic) {
                selectedFragment = new AcademicFragment();
            } else if (itemId == R.id.nav_event) {
                selectedFragment = new EventsFragment();
            } else if (itemId == R.id.nav_all) {
                selectedFragment = new AllFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
            Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
