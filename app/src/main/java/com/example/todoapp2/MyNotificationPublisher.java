package com.example.todoapp2;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class MyNotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.i("ExampleDialog", "notification shown");

        Notification notification = intent.getParcelableExtra("notification");
        int notificationId = intent.getIntExtra("notificationId", 0);
        notificationManager.notify(2, notification);
    }
}
