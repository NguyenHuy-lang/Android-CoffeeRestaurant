package com.example.mycoffee.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mycoffee.R;
import com.example.mycoffee.model.User;

import java.io.Serializable;
import java.util.List;

public class NotificationUtils {

    private static final String CHANNEL_ID = "default_channel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "Default Channel Description";

    public static void showNotification(Context context, String title,
                                        String message, Class<?> targetActivityClass, User user, String sequenceId) {
        // Create a notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a full-screen intent
        Intent fullScreenIntent = new Intent(context, targetActivityClass);
        fullScreenIntent.putExtra("object_user", user);
        fullScreenIntent.putExtra("order_ids", sequenceId);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context,
                0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(true);

        // Set the content intent
        builder.setContentIntent(fullScreenPendingIntent);

        // Display the notification
        notificationManager.notify(0, builder.build());
    }
}
