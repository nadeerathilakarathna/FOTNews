package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class DeveloperActivity extends AppCompatActivity {
    private static final String TAG = "FOTNewsLog";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectLogin(this);

        UiHelper.setStatusBarandNavigationBarColor(DeveloperActivity.this, R.color.color_primary, R.color.color_primary);

        setContentView(R.layout.activity_developer);
        MaterialToolbar devAppBar = findViewById(R.id.devAppBar);
        setSupportActionBar(devAppBar);

        UiHelper.setStatusBarPadding(DeveloperActivity.this, devAppBar);


        devAppBar.setNavigationOnClickListener(view -> {
                        Intent intent = new Intent(DeveloperActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DeveloperActivity.this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

}
