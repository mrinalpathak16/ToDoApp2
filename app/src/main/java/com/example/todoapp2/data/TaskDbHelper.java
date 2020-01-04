package com.example.todoapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todoapp2.data.TaskContract.TaskEntry;

public class TaskDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = TaskDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "todo.db";

    private static final int DATABASE_VERSION = 1;

    public TaskDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskEntry.COLUMN_TASK_TYPE + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_TASK_LABEL + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_TASK_DESCRIPTION + " TEXT, " +
                TaskEntry.COLUMN_NOTIFICATION_TIME + " INTEGER NOT NULL, "+
                TaskEntry.COLUMN_USER_ID + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_TASK_STATUS + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
