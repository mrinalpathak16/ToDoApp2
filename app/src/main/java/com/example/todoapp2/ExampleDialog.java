package com.example.todoapp2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.todoapp2.data.TaskContract.TaskEntry;


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

    public void setValues(Context context, Uri uri, Cursor cursor){
        mContext =context;
        mUri = uri;
        mCursor = cursor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        String title;
        if(mUri==null){
            title="Add Task";
        }
        else{
            title="Edit Task";
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
                        saveTask();
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

        return builder.create();
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

        //TODO: Galti se press ho gaya wala condition

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_TASK_TYPE, type);
        values.put(TaskEntry.COLUMN_TASK_LABEL,taskLabel);
        values.put(TaskEntry.COLUMN_TASK_DESCRIPTION, taskDesc);
        values.put(TaskEntry.COLUMN_NOTIFICATION_TIME, notifTime);

        Uri savedUri;String message;
        if(mUri == null){
            savedUri = mContext.getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            if(savedUri!=null){
                message = "Pet inserted";
            }
            else{
                message = "error with saving pet";
            }

        }
        else{
            int i = mContext.getContentResolver().update(mUri, values, null, null);
            if(i!=-1){
                message = "Pet updated";
            }
            else{
                message = "error with updating pet";
            }
        }
        Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();

    }
}

