package com.example.davonlineshop.ui.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.davonlineshop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification());

    }

    private void showNotification(RemoteMessage.Notification notification) {
        String noteId = "com.example.davonlineshop";
        Intent noteIntent = new Intent(this, MyFireBaseMessagingService.class);
        noteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noteIntent, 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, noteId)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_baseline_notifications)
                        .setContentInfo("info")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setSubText("Good")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(new Random().nextInt(), builder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        showNotification();
    }

    private void showNotification() {
        String noteId = "com.example.davonlineshop";
        Intent noteIntent = new Intent(this, MyFireBaseMessagingService.class);
                noteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noteIntent, 0);
        NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this, noteId)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setSmallIcon(R.drawable.ic_baseline_notifications)
                                .setContentInfo("info")
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle("Title")
                                .setContentText("Notification text")
                                .setSubText("Good")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText("Bareev dzezzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }
                notificationManager.notify(new Random().nextInt(), builder.build());
            }

}
