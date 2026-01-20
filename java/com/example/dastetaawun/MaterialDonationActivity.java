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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MaterialDonationActivity extends AppCompatActivity {

    private Spinner itemTypeSpinner;
    private EditText itemDescriptionEditText, itemConditionEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_donation);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
    }

    private void initializeViews() {
        itemTypeSpinner = findViewById(R.id.itemTypeSpinner);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);
        itemConditionEditText = findViewById(R.id.itemConditionEditText);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        submitButton.setOnClickListener(v -> submitDonation());
    }

    private void submitDonation() {
        String itemType = itemTypeSpinner.getSelectedItem().toString();
        String description = itemDescriptionEditText.getText().toString().trim();
        String condition = itemConditionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            itemDescriptionEditText.setError("Description is required");
            return;
        }

        if (TextUtils.isEmpty(condition)) {
            itemConditionEditText.setError("Condition is required");
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Map<String, Object> materialDonation = new HashMap<>();
        materialDonation.put("donorId", currentUser.getUid());
        materialDonation.put("itemType", itemType);
        materialDonation.put("description", description);
        materialDonation.put("condition", condition);
        materialDonation.put("status", "pending");
        materialDonation.put("timestamp", com.google.firebase.Timestamp.now());

        db.collection("material_donations")
                .add(materialDonation)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Material donation submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}