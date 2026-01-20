package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView nameTextView, emailTextView, phoneTextView, userTypeTextView;
    private TextView donationsCountTextView, volunteerHoursTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadUserProfile();
    }

    private void initializeViews() {
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        userTypeTextView = findViewById(R.id.userTypeTextView);
        donationsCountTextView = findViewById(R.id.donationsCountTextView);
        volunteerHoursTextView = findViewById(R.id.volunteerHoursTextView);
        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());

            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String phone = documentSnapshot.getString("phone");
                            String userType = documentSnapshot.getString("userType");

                            nameTextView.setText(name != null ? name : "N/A");
                            phoneTextView.setText(phone != null ? phone : "N/A");
                            userTypeTextView.setText(userType != null ? userType.toUpperCase() : "N/A");
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                    );

            // Load donation count
            db.collection("donations")
                    .whereEqualTo("donorId", currentUser.getUid())
                    .get()
                    .addOnSuccessListener(querySnapshot ->
                            donationsCountTextView.setText(String.valueOf(querySnapshot.size()))
                    );
        }
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}