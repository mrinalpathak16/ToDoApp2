package com.example.todoapp2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.todoapp2.data.TaskContract.TaskEntry;

public class DetailsDialog extends AppCompatDialogFragment {
    private TextView typeText, labelText, descText, timeText, dateText;
    private Context mContext;
    private Cursor mCursor;
    private Uri mUri;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.details_dialog, null);

        String title;
        if(mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_STATUS))==0)
            title="Edit";
        else
            title = "Reset";

        builder.setView(view)
                .setTitle("Task Details")
                .setNegativeButton(title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OngoingFragment.EditDialogListener listener =
                                (OngoingFragment.EditDialogListener)mContext;
                        listener.openDialog(mUri, mCursor);
                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.getContentResolver().delete(mUri,null,null);
                        int Id = Integer.parseInt(mUri.getLastPathSegment());
                        ExampleDialog dialog1 = new ExampleDialog();
                        dialog1.cancelAlarm(Id, mContext);
                    }
                });

        typeText = view.findViewById(R.id.typeText);
        String typeT;
        if(mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_TYPE))==0){
            typeT = "Normal Task";
        }
        else {
            typeT = "Priority Task";
        }
        typeText.setText(typeT);

        labelText = view.findViewById(R.id.labelText);
        labelText.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_LABEL)));

        descText = view.findViewById(R.id.descText);
        String d = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskEntry.COLUMN_TASK_DESCRIPTION));
        if(d.equals("")){
            LinearLayout descRoot = view.findViewById(R.id.descRoot);
            descRoot.setVisibility(View.GONE);
        }
        else{
            descText.setText(d);
        }

        TimeDateFormat obj = new TimeDateFormat(mCursor.getLong(mCursor.getColumnIndexOrThrow(
                TaskEntry.COLUMN_NOTIFICATION_TIME
        )));

        timeText = view.findViewById(R.id.timeText);
        timeText.setText(obj.getHour()+":"+obj.getMinute());

        dateText = view.findViewById(R.id.dateText);
        dateText.setText(obj.getDayOfMonth()+"/"+obj.getMonth()+"/"+obj.getYear());

        return builder.create();
    }

    public void setVal(Context context, Cursor cursor, Uri uri){
        this.mUri = uri;
        this.mContext = context;
        this.mCursor = cursor;
    }
}
