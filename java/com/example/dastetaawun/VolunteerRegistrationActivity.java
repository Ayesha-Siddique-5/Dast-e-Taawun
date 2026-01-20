package com.example.dastetaawun;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dastetaawun.models.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class VolunteerRegistrationActivity extends AppCompatActivity {

    private EditText availabilityEditText, locationEditText;
    private CheckBox teachingCheckBox, medicalCheckBox, logisticsCheckBox, fundingCheckBox;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_registration);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
    }

    private void initializeViews() {
        availabilityEditText = findViewById(R.id.availabilityEditText);
        locationEditText = findViewById(R.id.locationEditText);
        teachingCheckBox = findViewById(R.id.teachingCheckBox);
        medicalCheckBox = findViewById(R.id.medicalCheckBox);
        logisticsCheckBox = findViewById(R.id.logisticsCheckBox);
        fundingCheckBox = findViewById(R.id.fundingCheckBox);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(v -> registerVolunteer());
    }

    private void registerVolunteer() {
        String availability = availabilityEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(availability)) {
            availabilityEditText.setError("Availability is required");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            locationEditText.setError("Location is required");
            return;
        }

        List<String> skills = new ArrayList<>();
        if (teachingCheckBox.isChecked()) skills.add("Teaching & Education");
        if (medicalCheckBox.isChecked()) skills.add("Medical & Healthcare");
        if (logisticsCheckBox.isChecked()) skills.add("Logistics & Transportation");
        if (fundingCheckBox.isChecked()) skills.add("Fundraising & Marketing");

        if (skills.isEmpty()) {
            Toast.makeText(this, "Please select at least one skill", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String phone = documentSnapshot.getString("phone");

                    Volunteer volunteer = new Volunteer(
                            currentUser.getUid(),
                            name,
                            email,
                            phone,
                            location,
                            availability,
                            skills
                    );

                    db.collection("volunteers")
                            .add(volunteer)
                            .addOnSuccessListener(documentReference -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Registered as volunteer successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                });
    }
}