package com.example.dastetaawun;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

// YEH IMPORT ADD KAREIN
import com.example.dastetaawun.utils.FirebaseAuthHelper;
import com.example.dastetaawun.utils.FirestoreHelper;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Test Firebase Authentication
        testFirebaseAuth();

        // Test Firestore
        testFirestore();
    }

    private void testFirebaseAuth() {
        FirebaseAuthHelper authHelper = new FirebaseAuthHelper(this);

        // Test Registration
        authHelper.registerUser("test@example.com", "password123", "Test User",
                new FirebaseAuthHelper.AuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, "Registration Success: " + message);

                        // Test Login
                        authHelper.loginUser("test@example.com", "password123",
                                new FirebaseAuthHelper.AuthCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Log.d(TAG, "Login Success: " + message);
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Log.e(TAG, "Login Failed: " + error);
                                    }
                                });
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e(TAG, "Registration Failed: " + error);
                    }
                });
    }

    private void testFirestore() {
        FirestoreHelper firestoreHelper = new FirestoreHelper();

        // Test User Save
        firestoreHelper.saveUser("user123", "John Doe", "john@example.com",
                "03001234567", "donor", new FirestoreHelper.FirestoreCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, "Firestore Save Success: " + message);
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e(TAG, "Firestore Save Failed: " + error);
                    }
                });
    }
}