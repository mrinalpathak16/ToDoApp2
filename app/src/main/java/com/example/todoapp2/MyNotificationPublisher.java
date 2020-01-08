package com.example.todoapp2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp2.data.TaskContract;

import java.util.concurrent.TimeUnit;

public class MyNotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.i("ExampleDialog", "notification shown");

        Notification notification = intent.getParcelableExtra("notification");
        int notificationId = intent.getIntExtra("notificationId", 0);
        notificationManager.notify(notificationId, notification);

        int no = intent.getIntExtra("number", 1);
        if(no == 3||no==2){
            long time;
            time = System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(30);
            if(no==3){
                time += TimeUnit.HOURS.toMillis(3);
                no = 2;
            }
            else
                no = 1;
            Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
            notificationIntent.putExtra("number", no);
            notificationIntent.putExtra("notificationId", notificationId);
            notificationIntent.setData(Uri.withAppendedPath(TaskContract.TaskEntry.CONTENT_URI,
                    String.valueOf(notificationId)));
            notificationIntent.putExtra("notification", notification);
            PendingIntent pI = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pI);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pI);
        }
        else{
            Uri uri = intent.getData();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COLUMN_TASK_STATUS, TaskContract.TaskEntry.ONGOING_TASK);
            context.getContentResolver().update(uri, values, null, null);
        }
    }
}
