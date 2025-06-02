package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;

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

        TextView txt_username = findViewById(R.id.user_name);
        TextView txt_useremail = findViewById(R.id.user_email);

        TextInputEditText inp_username = findViewById(R.id.edit_name);
        TextInputEditText inp_email = findViewById(R.id.edit_email);
        TextInputEditText inp_password = findViewById(R.id.edit_password);
        TextInputEditText inp_cpassword = findViewById(R.id.edit_cpassword);
        TextInputEditText inp_oldpassword = findViewById(R.id.edit_oldpassword);

        FirebaseHelper.getUserDetails(ProfileActivity.this, txt_username, txt_useremail,inp_username,inp_email,inp_oldpassword,inp_password,inp_cpassword);



        profileAppBar.setNavigationOnClickListener(view -> {
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

        alert_background.setVisibility(View.GONE);
        alert_edit_profile.setVisibility(View.GONE);
        alert_logout.setVisibility(View.GONE);



        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inp_password.setText("");
                inp_cpassword.setText("");
                inp_oldpassword.setText("");
                alert_background.setVisibility(View.VISIBLE);
                alert_edit_profile.setVisibility(View.VISIBLE);
            }
        });

        btn_close_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.getUserDetails(ProfileActivity.this, txt_username, txt_useremail,inp_username,inp_email,inp_oldpassword,inp_password,inp_cpassword);
                alert_background.setVisibility(View.GONE);
                alert_edit_profile.setVisibility(View.GONE);

            }
        });

        btn_edit_profile_save.setOnClickListener(v -> {
            String newUsername = inp_username.getText().toString().trim();
            String oldPassword = inp_oldpassword.getText().toString().trim();
            String newPassword = inp_password.getText().toString().trim();
            String confirmPassword = inp_cpassword.getText().toString().trim();

            if (newUsername.isEmpty()) {
                inp_username.setError("Username required");
                return;
            }

            if (!(oldPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty())) {

                if (oldPassword.isEmpty()) {
                    inp_oldpassword.setError("Old password required");
                    return;
                }

                if ((newPassword.isEmpty() || newPassword.length() < 8)) {
                    inp_password.setError("Password must be 8+ characters");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    inp_cpassword.setError("Please input confirm password");
                    return;
                }
                if (!confirmPassword.equals(newPassword)) {
                    inp_cpassword.setError("Password does not match");
                    return;
                }
            }

            FirebaseHelper.updateUserProfile(ProfileActivity.this, newUsername, oldPassword, newPassword, () -> {
                alert_background.setVisibility(View.GONE);
                alert_edit_profile.setVisibility(View.GONE);
            });
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
