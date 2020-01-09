package com.example.todoapp2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp2.data.TaskContract.TaskEntry;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class ExampleDialog extends AppCompatDialogFragment {
    private EditText label, desc;
    private RadioButton normal, priority;
    private TextView normalText, priorityText;
    private CheckBox checkBox;
    private TimePicker time;
    private DatePicker date;

    public static final String LOG_TAG = ExampleDialog.class.getSimpleName();

    private Uri mUri;
    private Context mContext;
    private Cursor mCursor;
    private String mUsername;
    private String mUid;

    public void setValues(Context context, Uri uri, Cursor cursor, String username, String uid){
        mContext =context;
        mUri = uri;
        mCursor = cursor;
        mUsername = username;
        mUid = uid;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        String title;
        if(mUri==null){
            title="Schedule New Task";
        }
        else{
            if(mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_STATUS))==0)
                title="Edit Task";
            else
                title = "Reset Task";
        }

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normal = view.findViewById(R.id.normalTask);
        priority = view.findViewById(R.id.priorityTask);
        normalText = view.findViewById(R.id.normalTaskText);
        priorityText = view.findViewById(R.id.priorityTaskText);
        normalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.toggle();
            }
        });
        priorityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority.toggle();
            }
        });
        label = view.findViewById(R.id.label);
        checkBox = view.findViewById(R.id.checkBox);
        desc = view.findViewById(R.id.desc);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked())
                    desc.setVisibility(View.VISIBLE);
                else {
                    desc.setVisibility(View.GONE);
                    desc.setText("");
                }
            }
        });
        time = view.findViewById(R.id.time);
        time.setIs24HourView(true);
        date = view.findViewById(R.id.date);
        date.setMinDate(System.currentTimeMillis());

        if(mUri!=null){
            int taskT = mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_TYPE));
            if(taskT==0)
                normal.setChecked(true);
            else
                priority.setChecked(true);
            label.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_LABEL)));
            String taskD = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_DESCRIPTION));
            if(taskD.equals("")){
                checkBox.setChecked(false);
                desc.setVisibility(View.GONE);
            }
            else {
                checkBox.setChecked(true);
                desc.setVisibility(View.VISIBLE);
            }
            desc.setText(taskD);

            TimeDateFormat obj = new TimeDateFormat(mCursor.getLong(mCursor.getColumnIndexOrThrow(
                    TaskEntry.COLUMN_NOTIFICATION_TIME
            )));

            time.setMinute(Integer.parseInt(obj.getMinute()));
            time.setHour(Integer.parseInt(obj.getHour()));

            date.updateDate(Integer.parseInt(obj.getYear()),(Integer.parseInt(obj.getMonth())-1),
                    Integer.parseInt(obj.getDayOfMonth()));
        }

        AlertDialog alertDialog =  builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c = Calendar.getInstance();
                        c.set(date.getYear(), date.getMonth(), date.getDayOfMonth(),
                                time.getHour(), time.getMinute(), 0);
                        long t = c.getTimeInMillis();
                        if(!(label.getText().toString().equals(""))&&t>System.currentTimeMillis()){
                            saveTask();
                            dialog.dismiss();
                        }
                        else {
                            if(label.getText().toString().equals(""))
                                Toast.makeText(mContext, "Task Label Required!", Toast.LENGTH_SHORT).
                                        show();
                            else
                                Toast.makeText(mContext, "Please insert a time in future!",
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    public void saveTask(){
        int type;
        if(normal.isChecked()){
            type = 0;
        }
        else{
            type = 1;
        }
        String taskLabel = label.getText().toString();
        String taskDesc = desc.getText().toString();
        long notifTime = ((long)date.getYear())*100000000 + (date.getMonth()+1)*1000000 +
                date.getDayOfMonth()*10000 + time.getHour()*100 + time.getMinute();

        Log.i(LOG_TAG,""+notifTime);

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_TASK_TYPE, type);
        values.put(TaskEntry.COLUMN_TASK_LABEL,taskLabel);
        values.put(TaskEntry.COLUMN_TASK_DESCRIPTION, taskDesc);
        values.put(TaskEntry.COLUMN_NOTIFICATION_TIME, notifTime);
        values.put(TaskEntry.COLUMN_USER_ID, mUid);
        values.put(TaskEntry.COLUMN_TASK_STATUS, TaskEntry.SCHEDULED_TASK);

        Uri savedUri;String message;

        Calendar c = Calendar.getInstance();
        c.set(date.getYear(), date.getMonth(), date.getDayOfMonth(),
                time.getHour(), time.getMinute(), 0);
        long time = c.getTimeInMillis();

        int no = 1;

        if(type==1) {
            if(time-TimeUnit.HOURS.toMillis(4)>=System.currentTimeMillis()){
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

        if(mUri == null){
            savedUri = mContext.getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            if(savedUri!=null){
                message = "Pet inserted";
                int Id = Integer.parseInt(savedUri.getLastPathSegment());
                setAlarm(time, Id, type, no, label.getText().toString(), mUsername, desc.getText().toString());
                Log.i("ExampleDialog", "alarm set");
            }
            else{
                message = "error with saving pet";
            }

        }
        else{
            int i = mContext.getContentResolver().update(mUri, values, null, null);
            if(i!=-1){
                message = "Pet updated";
                int Id = Integer.parseInt(mUri.getLastPathSegment());
                cancelAlarm(Id, mContext);
                setAlarm(time,Id, type, no, label.getText().toString(), mUsername, desc.getText().toString());
            }
            else{
                message = "error with updating pet";
            }
        }
        Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();

    }


    public void setAlarm(long t, int Id, int type, int no, String taskLabel, String username,
                         String taskDescription){

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        Bitmap icon;
        if (type==0){
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        }
        else {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.priority);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "channelId")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle(taskLabel)
                .setContentText(username + " has a pending task!")
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(taskDescription))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        Notification notification = builder.build();

        Log.i("ExampleDialog", "built!"+t);

        Intent notificationIntent = new Intent(mContext, MyNotificationPublisher.class);
        notificationIntent.putExtra("number", no);
        notificationIntent.putExtra("notificationId", Id);
        notificationIntent.setData(Uri.withAppendedPath(TaskEntry.CONTENT_URI,String.valueOf(Id)));
        notificationIntent.putExtra("notification", notification);
        PendingIntent pI = PendingIntent.getBroadcast(
                mContext,
                Id,
                notificationIntent,
                0);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, t, pI);

    }

    public void cancelAlarm(int Id, Context context){
        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        PendingIntent pI = PendingIntent.getBroadcast(
                context,
                Id,
                notificationIntent,
                0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pI);
    }
}

