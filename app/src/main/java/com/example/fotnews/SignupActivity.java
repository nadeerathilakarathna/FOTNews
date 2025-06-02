package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText edit_name,edit_email,edit_password, edit_cpassword;
    private String name, email, password, cpassword;
    private Button btn_signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectFeed(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_signup);

        edit_name = findViewById(R.id.name);
        edit_email = findViewById(R.id.email);
        edit_password = findViewById(R.id.password);
        edit_cpassword = findViewById(R.id.cpassword);
        btn_signup = findViewById(R.id.signupButton);

        name = edit_name.toString().trim();
        email = edit_email.toString().trim();
        password = edit_password.toString().trim();
        cpassword = edit_cpassword.toString().trim();





        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get trimmed values
                String name = edit_name.getText().toString().trim();
                String email = edit_email.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                String cpassword = edit_cpassword.getText().toString().trim();

                // Validate empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password length
                if (password.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate match
                if (!password.equals(cpassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register using helper
                FirebaseHelper.registerUser(name, email, password, SignupActivity.this);
            }
        });








        TextView loginText = findViewById(R.id.tvNavigateSignup);

        loginText.setText(Html.fromHtml("Already have an Account: <span style=\"color:#607df3\";>LOG IN</span>"));
        loginText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

    }
}
