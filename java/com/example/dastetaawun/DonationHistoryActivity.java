package com.example.dastetaawun;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dastetaawun.adapters.DonationAdapter;
import com.example.dastetaawun.models.Donation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DonationHistoryActivity extends AppCompatActivity {

    private RecyclerView donationsRecyclerView;
    private DonationAdapter donationAdapter;
    private List<Donation> donationList;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        loadDonations();
    }

    private void initializeViews() {
        donationsRecyclerView = findViewById(R.id.donationsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        donationList = new ArrayList<>();
        donationAdapter = new DonationAdapter(this, donationList);
        donationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donationsRecyclerView.setAdapter(donationAdapter);
    }

    private void loadDonations() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            progressBar.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
            return;
        }

        db.collection("donations")
                .whereEqualTo("donorId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    donationList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Donation donation = document.toObject(Donation.class);
                            donation.setDonationId(document.getId());
                            donationList.add(donation);
                        }
                        donationAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                });
    }
}