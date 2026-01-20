package com.example.dastetaawun.utils;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseAuthHelper {
    private FirebaseAuth mAuth;
    private Context context;

    public FirebaseAuthHelper(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    // 1. User Registration
    public void registerUser(String email, String password, String name,
                             AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // Update user profile with name
                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            callback.onSuccess(user.getUid());
                                        } else {
                                            if (updateTask.getException() != null) {
                                                callback.onFailure(updateTask.getException().getMessage());
                                            } else {
                                                callback.onFailure("Unknown error updating profile");
                                            }
                                        }
                                    });
                        } else {
                            callback.onFailure("User is null after registration");
                        }
                    } else {
                        if (task.getException() != null) {
                            callback.onFailure(task.getException().getMessage());
                        } else {
                            callback.onFailure("Unknown registration error");
                        }
                    }
                });
    }

    // 2. User Login
    public void loginUser(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            callback.onSuccess(user.getUid());
                        } else {
                            callback.onFailure("User is null after login");
                        }
                    } else {
                        if (task.getException() != null) {
                            callback.onFailure(task.getException().getMessage());
                        } else {
                            callback.onFailure("Unknown login error");
                        }
                    }
                });
    }

    // 3. Password Reset
    public void resetPassword(String email, AuthCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Reset email sent");
                    } else {
                        if (task.getException() != null) {
                            callback.onFailure(task.getException().getMessage());
                        } else {
                            callback.onFailure("Unknown error sending reset email");
                        }
                    }
                });
    }

    // 4. Get Current User
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // 5. Logout
    public void logout() {
        mAuth.signOut();
    }

    // Callback Interface
    public interface AuthCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
}