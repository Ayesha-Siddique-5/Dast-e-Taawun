package com.example.dastetaawun;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dastetaawun.adapters.VolunteerAdapter;
import com.example.dastetaawun.models.Volunteer;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class VolunteerListActivity extends AppCompatActivity {

    private RecyclerView volunteersRecyclerView;
    private VolunteerAdapter volunteerAdapter;
    private List<Volunteer> volunteerList;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadVolunteers();
    }

    private void initializeViews() {
        volunteersRecyclerView = findViewById(R.id.volunteersRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        volunteerList = new ArrayList<>();
        volunteerAdapter = new VolunteerAdapter(this, volunteerList);
        volunteersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteersRecyclerView.setAdapter(volunteerAdapter);
    }

    private void loadVolunteers() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);

        db.collection("volunteers")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    volunteerList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Volunteer volunteer = document.toObject(Volunteer.class);
                            volunteer.setVolunteerId(document.getId());
                            volunteerList.add(volunteer);
                        }
                        volunteerAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Failed to load volunteers", Toast.LENGTH_SHORT).show();
                });
    }
}