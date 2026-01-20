package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // UI Elements
    private Toolbar toolbar;
    private TextView welcomeTextView, userNameTextView;
    private Button applyCharityButton, donateButton, volunteerButton, eventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);


        // Initialize UI elements
        initializeViews();

        // Load user data
        loadUserData();

        // Setup button click listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        applyCharityButton = findViewById(R.id.applyCharityButton);
        donateButton = findViewById(R.id.donateButton);
        volunteerButton = findViewById(R.id.volunteerButton);
        eventsButton = findViewById(R.id.eventsButton);
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Load user details from Firestore
            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            if (name != null) {
                                welcomeTextView.setText("Welcome!");
                                userNameTextView.setText(name);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this,
                                "Failed to load user data",
                                Toast.LENGTH_SHORT).show();
                    });
        } else {
            // User not logged in, redirect to login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void setupButtonListeners() {
        // Request Charity Help
        applyCharityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ApplyCharityActivity.class);
            startActivity(intent);
        });

        // Make a Donation
        donateButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DonationActivity.class);
            startActivity(intent);
        });

        // Volunteer
        volunteerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VolunteerRegistrationActivity.class);
            startActivity(intent);
        });

        // View Events
        eventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Check if user is admin and show/hide admin menu item
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userType = documentSnapshot.getString("userType");
                            MenuItem adminItem = menu.findItem(R.id.action_admin);
                            if (adminItem != null) {
                                adminItem.setVisible("admin".equals(userType));
                            }
                        }
                    });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            // Open Profile Activity
            startActivity(new Intent(this, ProfileActivity.class));
            return true;

        } else if (id == R.id.action_admin) {
            // Open Admin Dashboard
            startActivity(new Intent(this, AdminDashboardActivity.class));
            return true;

        } else if (id == R.id.action_logout) {
            // Logout user
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        // Sign out from Firebase
        mAuth.signOut();

        // Show message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to Login Activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data when returning to this activity
        loadUserData();
    }
}