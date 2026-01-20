package com.example.dastetaawun.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationHelper {
    private Context context;
    private static final String CHANNEL_ID = "dastetaawun_channel";

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void showNotification(String title, String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Dastetaawun Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Send FCM notification to a specific token
    public void sendFCMNotification(String token, String title, String message) {
        // TODO: Use Firebase Cloud Messaging HTTP API or your server to send message
        // This is a placeholder. In real app, send POST request to FCM endpoint with token, title, body
    }
}
