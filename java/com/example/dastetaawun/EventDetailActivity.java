package com.example.dastetaawun;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dastetaawun.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {

    private TextView eventTitleTextView, eventDateTextView, eventLocationTextView;
    private TextView eventDescriptionTextView, progressTextView, amountTextView;
    private ProgressBar progressBar;
    private Button joinEventButton;
    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        db = FirebaseFirestore.getInstance();
        eventId = getIntent().getStringExtra("eventId");

        initializeViews();
        loadEventDetails();
    }

    private void initializeViews() {
        eventTitleTextView = findViewById(R.id.eventTitleTextView);
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventLocationTextView = findViewById(R.id.eventLocationTextView);
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
        progressTextView = findViewById(R.id.progressTextView);
        amountTextView = findViewById(R.id.amountTextView);
        progressBar = findViewById(R.id.progressBar);
        joinEventButton = findViewById(R.id.joinEventButton);

        joinEventButton.setOnClickListener(v -> joinEvent());
    }

    private void loadEventDetails() {
        if (eventId == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if (event != null) {
                            displayEventDetails(event);
                        }
                    } else {
                        Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load event", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayEventDetails(Event event) {
        eventTitleTextView.setText(event.getTitle());
        eventLocationTextView.setText(event.getLocation());
        eventDescriptionTextView.setText(event.getDescription());

        if (event.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            eventDateTextView.setText(sdf.format(event.getDate().toDate()));
        }

        int progress = event.getProgressPercentage();
        progressBar.setProgress(progress);
        progressTextView.setText(progress + "% Complete");

        String amountText = "PKR " + String.format(Locale.getDefault(), "%.0f", event.getCurrentAmount()) +
                " / PKR " + String.format(Locale.getDefault(), "%.0f", event.getTargetAmount());
        amountTextView.setText(amountText);
    }

    private void joinEvent() {
        Toast.makeText(this, "Event registration - Coming soon!", Toast.LENGTH_SHORT).show();
    }
}