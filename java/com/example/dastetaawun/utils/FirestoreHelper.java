package com.example.dastetaawun.utils;

import android.util.Log;

import com.example.dastetaawun.utils.NotificationHelper;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db;
    private static final String TAG = "FirestoreHelper";

    public FirestoreHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    // 1️⃣ Save User
    public void saveUser(String userId, String name, String email,
                         String phone, String userType, FirestoreCallback callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("userType", userType);
        user.put("createdAt", new java.util.Date());

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User saved successfully");
                    callback.onSuccess("User saved successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving user", e);
                    callback.onFailure(e.getMessage());
                });
    }

    // 2️⃣ Get User (READ)
    public void getUser(String uid, UserCallback callback) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        callback.onSuccess(document);
                    } else {
                        callback.onFailure("User not found");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 3️⃣ Save FCM token
    public void saveUserToken(String userId, String token, FirestoreCallback callback) {
        db.collection("users").document(userId)
                .update("fcmToken", token)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Token saved"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 4️⃣ Create Donation + Notify Admins
    public void createDonation(String donationId, String donorId, String donationType,
                               double amount, String itemName, FirestoreCallback callback) {
        Map<String, Object> donation = new HashMap<>();
        donation.put("donationId", donationId);
        donation.put("donorId", donorId);
        donation.put("donationType", donationType);
        donation.put("amount", amount);
        donation.put("itemName", itemName);
        donation.put("status", "pending");
        donation.put("donationDate", new java.util.Date());

        db.collection("donations").document(donationId)
                .set(donation)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess("Donation created");
                    sendAdminNotification(donationType, amount);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 🔔 Send notification to admins
    private void sendAdminNotification(String donationType, double amount) {
        db.collection("users")
                .whereEqualTo("userType", "admin")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String token = doc.getString("fcmToken");
                        /*if (token != null && !token.isEmpty()) {
                            NotificationHelper helper = new NotificationHelper(App.getContext());
                            helper.sendFCMNotification(token,
                                    "New Donation Received",
                                    donationType + " of amount $" + amount);
                        }*/
                    }
                });
    }

    // 5️⃣ Create Event
    public void createEvent(String eventId, String eventName, String description,
                            String location, String eventDate, FirestoreCallback callback) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", eventId);
        event.put("eventName", eventName);
        event.put("description", description);
        event.put("location", location);
        event.put("eventDate", eventDate);
        event.put("targetVolunteers", 0);
        event.put("registeredVolunteers", 0);
        event.put("createdAt", new java.util.Date());

        db.collection("events").document(eventId)
                .set(event)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Event created"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 6️⃣ Submit Charity Application
    public void submitApplication(String applicationId, String applicantId,
                                  String applicationType, String description,
                                  double amount, FirestoreCallback callback) {
        Map<String, Object> application = new HashMap<>();
        application.put("applicationId", applicationId);
        application.put("applicantId", applicantId);
        application.put("applicationType", applicationType);
        application.put("description", description);
        application.put("requestedAmount", amount);
        application.put("status", "pending");
        application.put("appliedDate", new java.util.Date());

        db.collection("applications").document(applicationId)
                .set(application)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Application submitted"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 7️⃣ Update Application Status
    public void updateApplicationStatus(String applicationId, String newStatus,
                                        FirestoreCallback callback) {
        db.collection("applications").document(applicationId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Status updated"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 8️⃣ Register Volunteer
    public void registerVolunteer(String volunteerId, String userId,
                                  String[] skills, String availability,
                                  FirestoreCallback callback) {
        Map<String, Object> volunteer = new HashMap<>();
        volunteer.put("volunteerId", volunteerId);
        volunteer.put("userId", userId);
        volunteer.put("skills", Arrays.asList(skills));
        volunteer.put("availability", availability);
        volunteer.put("totalHours", 0);
        volunteer.put("completedEvents", 0);
        volunteer.put("registeredAt", new java.util.Date());

        db.collection("volunteers").document(volunteerId)
                .set(volunteer)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Volunteer registered"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // 9️⃣ Get All Events
    public Query getAllEvents() {
        return db.collection("events")
                .orderBy("eventDate", Query.Direction.ASCENDING);
    }

    // 🔟 Get Donations by User
    public Query getDonationsByUser(String userId) {
        return db.collection("donations")
                .whereEqualTo("donorId", userId)
                .orderBy("donationDate", Query.Direction.DESCENDING);
    }

    // 🔹 Interfaces
    public interface FirestoreCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface UserCallback {
        void onSuccess(DocumentSnapshot document);
        void onFailure(String error);
    }
}
