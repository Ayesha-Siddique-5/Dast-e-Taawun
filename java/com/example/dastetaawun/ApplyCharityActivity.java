package com.example.dastetaawun;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dastetaawun.models.CharityRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ApplyCharityActivity extends AppCompatActivity {

    private Spinner requestTypeSpinner;
    private EditText descriptionEditText, amountEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_charity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
    }

    private void initializeViews() {
        requestTypeSpinner = findViewById(R.id.requestTypeSpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        submitButton.setOnClickListener(v -> submitCharityRequest());
    }

    private void submitCharityRequest() {
        String requestType = requestTypeSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Description is required");
            return;
        }

        // Parse amount and make it final
        final double amount;
        if (!TextUtils.isEmpty(amountStr)) {
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                amountEditText.setError("Invalid amount");
                return;
            }
        } else {
            amount = 0;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        // Store final variables for lambda
        final String finalRequestType = requestType;
        final String finalDescription = description;

        // Get user name
        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String userName = documentSnapshot.exists() ?
                            documentSnapshot.getString("name") : "Anonymous";

                    CharityRequest request = new CharityRequest(
                            currentUser.getUid(),
                            userName,
                            finalRequestType,
                            finalDescription,
                            amount
                    );

                    db.collection("charity_requests").add(request)
                            .addOnSuccessListener(documentReference -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ApplyCharityActivity.this,
                                        "Request submitted successfully!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                submitButton.setEnabled(true);
                                Toast.makeText(ApplyCharityActivity.this,
                                        "Failed: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    submitButton.setEnabled(true);
                    Toast.makeText(ApplyCharityActivity.this,
                            "Failed to get user data",
                            Toast.LENGTH_SHORT).show();
                });
    }
}