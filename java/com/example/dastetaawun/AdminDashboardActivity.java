package com.example.dastetaawun;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView totalUsersTextView, totalDonationsTextView;
    private TextView totalRequestsTextView, totalVolunteersTextView;
    private Button manageRequestsButton, manageEventsButton, viewUsersButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadStatistics();
        setupButtons();
    }

    private void initializeViews() {
        totalUsersTextView = findViewById(R.id.totalUsersTextView);
        totalDonationsTextView = findViewById(R.id.totalDonationsTextView);
        totalRequestsTextView = findViewById(R.id.totalRequestsTextView);
        totalVolunteersTextView = findViewById(R.id.totalVolunteersTextView);
        manageRequestsButton = findViewById(R.id.manageRequestsButton);
        manageEventsButton = findViewById(R.id.manageEventsButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadStatistics() {
        progressBar.setVisibility(View.VISIBLE);

        // Load total users
        db.collection("users").get()
                .addOnSuccessListener(querySnapshot -> {
                    totalUsersTextView.setText(String.valueOf(querySnapshot.size()));
                });

        // Load total donations
        db.collection("donations").get()
                .addOnSuccessListener(querySnapshot -> {
                    totalDonationsTextView.setText(String.valueOf(querySnapshot.size()));
                });

        // Load total charity requests
        db.collection("charity_requests").get()
                .addOnSuccessListener(querySnapshot -> {
                    totalRequestsTextView.setText(String.valueOf(querySnapshot.size()));
                });

        // Load total volunteers
        db.collection("volunteers").get()
                .addOnSuccessListener(querySnapshot -> {
                    totalVolunteersTextView.setText(String.valueOf(querySnapshot.size()));
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupButtons() {
        manageRequestsButton.setOnClickListener(v ->
                Toast.makeText(this, "Manage Requests - Coming soon!", Toast.LENGTH_SHORT).show()
        );

        manageEventsButton.setOnClickListener(v ->
                Toast.makeText(this, "Manage Events - Coming soon!", Toast.LENGTH_SHORT).show()
        );

        viewUsersButton.setOnClickListener(v ->
                Toast.makeText(this, "View Users - Coming soon!", Toast.LENGTH_SHORT).show()
        );
    }
}