package com.example.dastetaawun.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dastetaawun.utils.FirestoreHelper;
import com.example.dastetaawun.utils.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.auth.FirebaseAuth;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            NotificationHelper helper = new NotificationHelper(this);
            helper.showNotification(title, body);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM", "Data payload: " + remoteMessage.getData().toString());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "New token: " + token);

        // Get logged-in user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (userId != null) {
            FirestoreHelper firestoreHelper = new FirestoreHelper();
            firestoreHelper.saveUserToken(userId, token, new FirestoreHelper.FirestoreCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("FCM", message);
                }
                @Override
                public void onFailure(String error) {
                    Log.e("FCM", error);
                }
            });
        } else {
            Log.d("FCM", "User not logged in, token not saved");
        }
    }

}
