package com.example.todoapp2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.todoapp2.data.TaskContract.TaskEntry;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AutoStartUp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String projection[] = {
                TaskEntry._ID,
                TaskEntry.COLUMN_TASK_TYPE,
                TaskEntry.COLUMN_TASK_LABEL,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskEntry.COLUMN_NOTIFICATION_TIME,
                TaskEntry.COLUMN_TASK_STATUS,
                TaskEntry.COLUMN_USER_ID
        };
        String selection = TaskEntry.COLUMN_TASK_STATUS+"=\'"+TaskEntry.SCHEDULED_TASK+"\'";

        Cursor cursor = context.getContentResolver().query(
                TaskEntry.CONTENT_URI,
                projection,
                selection,
                null,
                TaskEntry._ID
        );
        Log.i("mrinal", "cursor got");
        cursor.moveToFirst();

        for(int i = 0; i<cursor.getCount(); i++){

            Log.i("mrinal", "cursor no "+i+" "+cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_TASK_LABEL
            )));
            TimeDateFormat obj = new TimeDateFormat(cursor.getLong(cursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_NOTIFICATION_TIME
            )));
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(obj.getYear()),(Integer.parseInt(obj.getMonth())-1),
                    Integer.parseInt(obj.getDayOfMonth()), Integer.parseInt(obj.getHour()),
                    Integer.parseInt(obj.getMinute()), 0);
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(obj.getHour()));
            long time = c.getTimeInMillis();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry._ID));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_TYPE));
            String taskLabel = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_TASK_LABEL
            ));
            String username = "yoyobantairapper";
            String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_TASK_DESCRIPTION
            ));
            String uid = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_USER_ID
            ));
            String email = "hihi@haha.hmmmm";
            int no = 1;
            if(type==1) {
                if(time- TimeUnit.HOURS.toMillis(4)>=System.currentTimeMillis()){
                    time = time - TimeUnit.HOURS.toMillis(4);
                    no = 3;
                }
                else if(time-TimeUnit.MINUTES.toMillis(30)>=System.currentTimeMillis() &&
                        time-TimeUnit.HOURS.toMillis(4)<=System.currentTimeMillis()) {
                    time = time - TimeUnit.MINUTES.toMillis(30);
                    no = 2;
                }
                else {

                }

            }
            Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
            notificationIntent.putExtra("number", no);
            notificationIntent.putExtra("notificationId", id);
            notificationIntent.putExtra("type", type);
            notificationIntent.putExtra("tasklabel", taskLabel);
            notificationIntent.putExtra("username", username);
            notificationIntent.putExtra("taskdesc", taskDescription);
            notificationIntent.putExtra("uid", uid);
            notificationIntent.putExtra("userEmail", email);
            notificationIntent.setData(Uri.withAppendedPath(TaskEntry.CONTENT_URI,String.valueOf(id)));
            PendingIntent pI = PendingIntent.getBroadcast(
                    context,
                    id,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pI);

            cursor.moveToNext();
        }

    }
}
