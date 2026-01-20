package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dastetaawun.utils.FirebaseAuthHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotPasswordTextView;
    private ProgressBar progressBar;

    private FirebaseAuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authHelper = new FirebaseAuthHelper(this);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {

        loginButton.setOnClickListener(v -> loginUser());

        registerTextView.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        forgotPasswordTextView.setOnClickListener(v -> resetPassword());
    }

    private void loginUser() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email & Password required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        authHelper.loginUser(email, password,
                new FirebaseAuthHelper.AuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,
                                error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void resetPassword() {

        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email first");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        authHelper.resetPassword(email,
                new FirebaseAuthHelper.AuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,
                                "Reset email sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,
                                error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
