package com.example.todoapp2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp2.data.TaskContract;

import java.util.concurrent.TimeUnit;

import com.example.todoapp2.data.TaskContract.TaskEntry;

public class MyNotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.i("ExampleDialog", "notification shown");
        int notificationId = intent.getIntExtra("notificationId", 0);
        String taskLabel = intent.getStringExtra("tasklabel");
        String username = intent.getStringExtra("username");
        String taskDescription = intent.getStringExtra("taskdesc");
        Bitmap icon = intent.getParcelableExtra("bitmap");
        String uid = intent.getStringExtra("uid");
        String email = intent.getStringExtra("userEmail");
        int no = intent.getIntExtra("number", 1);
        String usernameExtra;

        //if this is the notification of a priority task
        if(no==3) {
             usernameExtra = "\'s priority task in 4 hours";
        }
        else if(no==2){
            usernameExtra = "\'s priority task in 30 minutes";
        }
        else{
            usernameExtra = " has a task to do";
        }


        //building notification
        Intent firstIntent = new Intent(context, MainActivity.class);
        firstIntent.putExtra("Uid", uid);
        firstIntent.putExtra("Email", email);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, firstIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle(taskLabel)
                .setContentText(username + usernameExtra)
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(taskDescription))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        Notification notification = builder.build();

        Log.i("ExampleDialog", "built!");

        notificationManager.notify(notificationId, notification);


        //to give successive alarms in case of priority tasks
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
            notificationIntent.putExtra("bitmap", icon);
            notificationIntent.putExtra("tasklabel", taskLabel);
            notificationIntent.putExtra("username", username);
            notificationIntent.putExtra("taskdesc", taskDescription);
            notificationIntent.setData(Uri.withAppendedPath(TaskEntry.CONTENT_URI,String.valueOf(
                    notificationId
            )));
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
