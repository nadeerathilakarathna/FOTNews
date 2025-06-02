package com.example.fotnews;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void handleResetError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(getApplicationContext(), "No user found with this email.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getApplicationContext(), "Invalid email format.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseTooManyRequestsException) {
            Toast.makeText(getApplicationContext(), "Too many requests. Try again later.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseNetworkException) {
            Toast.makeText(getApplicationContext(), "Network error. Check your connection.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Reset failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleLoginError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(this, "No account found with this email.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this, "Incorrect password or email.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseTooManyRequestsException) {
            Toast.makeText(this, "Too many attempts. Please try again later.", Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseNetworkException) {
            Toast.makeText(this, "Network issue. Check your internet.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Login failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            handleLoginError(e);
                        }
                    }
                });
    }


    public void sendPasswordReset(String email,LinearLayout alert_reset_password) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                        alert_reset_password.setVisibility(View.GONE);
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            handleResetError(e);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthCheck.redirectFeed(this);
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
        EditText edittext_reset_email = findViewById(R.id.reset_email);


        // Make text underlined
        forgotText.setText(Html.fromHtml("<u>Forgot Password?</u>"));
        signinText.setText(Html.fromHtml("You haven’t Account yet: <span style=\"color:#607df3\";>SIGN UP</span>"));

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                                               String username = usernameEditText.getText().toString();
                                               String password = passwordEditText.getText().toString();

                                               if (username.isEmpty()) {
                                                   Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                                                   return;
                                               }

                                               if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                                                   Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                                   return;
                                               }

                                               if (password.isEmpty()) {
                                                   Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                                                   return;
                                               }

                                               if (password.length() < 8) {
                                                   Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                                                   return;
                                               }

                                               loginUser(username, password);
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
                edittext_reset_email.setText("");
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

                if (edittext_reset_email.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    sendPasswordReset(edittext_reset_email.getText().toString(),alert_reset_password);
                }



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
