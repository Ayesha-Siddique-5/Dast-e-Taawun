package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dastetaawun.models.Donation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonationActivity extends AppCompatActivity {

    private RadioGroup paymentMethodRadioGroup;
    private RadioButton cashRadioButton, onlineRadioButton;
    private CheckBox anonymousCheckBox;
    private Button donateButton, materialDonationButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupButtons();
    }

    private void initializeViews() {
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        cashRadioButton = findViewById(R.id.cashRadioButton);
        onlineRadioButton = findViewById(R.id.onlineRadioButton);
        anonymousCheckBox = findViewById(R.id.anonymousCheckBox);
        donateButton = findViewById(R.id.donateButton);
        materialDonationButton = findViewById(R.id.materialDonationButton);
    }

    private void setupButtons() {
        donateButton.setOnClickListener(v -> processDonation());

        materialDonationButton.setOnClickListener(v -> {
            Intent intent = new Intent(DonationActivity.this, MaterialDonationActivity.class);
            startActivity(intent);
        });
    }

    private void processDonation() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = paymentMethodRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = selectedId == R.id.cashRadioButton ? "Cash" : "Online";
        boolean isAnonymous = anonymousCheckBox.isChecked();

        // Get user details
        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String donorName = isAnonymous ? "Anonymous" : documentSnapshot.getString("name");

                    // For demo purposes, using fixed amount and recipient
                    double amount = 1000.0; // You can add EditText for custom amount

                    Donation donation = new Donation(
                            currentUser.getUid(),
                            donorName,
                            "general_fund",
                            "General Charity Fund",
                            amount,
                            paymentMethod,
                            isAnonymous
                    );

                    saveDonation(donation);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to get user data", Toast.LENGTH_SHORT).show()
                );
    }

    private void saveDonation(Donation donation) {
        db.collection("donations")
                .add(donation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Donation successful! Thank you!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to process donation: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}