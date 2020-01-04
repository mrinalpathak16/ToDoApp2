package com.example.todoapp2;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp2.data.TaskContract;

public class MyNotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.i("ExampleDialog", "notification shown");

        Notification notification = intent.getParcelableExtra("notification");
        int notificationId = intent.getIntExtra("notificationId", 0);
        notificationManager.notify(notificationId, notification);

        Uri uri = intent.getData();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_STATUS, TaskContract.TaskEntry.ONGOING_TASK);
        context.getContentResolver().update(uri, values, null, null);
    }
}
