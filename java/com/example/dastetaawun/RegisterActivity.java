package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dastetaawun.utils.FirebaseAuthHelper;
import com.example.dastetaawun.utils.FirestoreHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText;
    private EditText passwordEditText, confirmPasswordEditText;
    private RadioGroup userTypeRadioGroup;
    private Button registerButton;
    private TextView loginTextView;
    private ProgressBar progressBar;

    private FirebaseAuthHelper authHelper;
    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authHelper = new FirebaseAuthHelper(this);
        firestoreHelper = new FirestoreHelper();

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Select user type", Toast.LENGTH_SHORT).show();
            return;
        }

        String userType =
                selectedId == R.id.donorRadioButton ? "donor" :
                        selectedId == R.id.volunteerRadioButton ? "volunteer" :
                                "applicant";

        progressBar.setVisibility(View.VISIBLE);

        authHelper.registerUser(email, password, name,
                new FirebaseAuthHelper.AuthCallback() {
                    @Override
                    public void onSuccess(String userId) {

                        firestoreHelper.saveUser(
                                userId,
                                name,
                                email,
                                phone,
                                userType,
                                new FirestoreHelper.FirestoreCallback() {

                                    @Override
                                    public void onSuccess(String message) {
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this,
                                                error, Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(String error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,
                                error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
