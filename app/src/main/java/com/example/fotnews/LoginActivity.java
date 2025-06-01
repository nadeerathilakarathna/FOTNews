package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView forgotText = findViewById(R.id.tvForgotPassword);
        TextView signinText = findViewById(R.id.tvNavigateSignup);
        LinearLayout alert_reset_password = findViewById(R.id.userview_frogot_password);
        ImageButton btn_reset_password_close = findViewById(R.id.close_froget_password);
        MaterialButton btn_reset_password_yes = findViewById(R.id.btn_froget_password_yes);
        MaterialButton btn_reset_password_no = findViewById(R.id.btn_froget_password_no);


        // Make text underlined
        forgotText.setText(Html.fromHtml("<u>Forgot Password?</u>"));
        signinText.setText(Html.fromHtml("You haven’t Account yet: <span style=\"color:#607df3\";>SIGN UP</span>"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.equals("") && password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to next activity if needed
                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_reset_password.setVisibility(View.VISIBLE);
            }
        });
        btn_reset_password_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_reset_password.setVisibility(View.GONE);
            }
        });
        btn_reset_password_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Reset Link sent successful", Toast.LENGTH_SHORT).show();
                alert_reset_password.setVisibility(View.GONE);
            }
        });
        btn_reset_password_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_reset_password.setVisibility(View.GONE);
            }
        });
    }
}
