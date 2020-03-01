package com.example.todoapp2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.todoapp2.data.TaskContract.TaskEntry;

import java.security.Provider;

public class TaskProvider extends ContentProvider {
    public static final String LOG_TAG = TaskProvider.class.getSimpleName();

    private static final int TASKS = 100;
    private static final int TASK_ID = 101;
    private static final int NAMES = 102;
    private static final int NAME_ID = 103;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASKS, TASKS);

        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY,TaskContract.PATH_TASKS+"/#",TASK_ID);

        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_NAMES, NAMES);

        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY,TaskContract.PATH_NAMES+"/#",NAME_ID);

    }

    private TaskDbHelper mDbHelper;
    @Override
    public boolean onCreate() {

        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                cursor = database.query(TaskEntry.TABLE_NAME,projection, selection, selectionArgs,
                        null,null,sortOrder);
                break;
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NAMES:
                cursor = database.query(TaskEntry.TABLE_1, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NAME_ID:
                selection = TaskEntry._ID1 + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(TaskEntry.TABLE_1, projection, selection, selectionArgs,
                        null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return insertTask(uri, contentValues);
            case NAMES:
                return insertName(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertName(Uri uri, ContentValues contentValues){
        String uid = contentValues.getAsString(TaskEntry.COLUMN_UID);
        if (uid==null) {
            throw new IllegalArgumentException("UID required");
        }

        String name = contentValues.getAsString(TaskEntry.COLUMN_NAME);
        if (name==null) {
            throw new IllegalArgumentException("Name required");
        }

        String email = contentValues.getAsString(TaskEntry.COLUMN_EMAIL);
        if (email==null) {
            throw new IllegalArgumentException("email required");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TaskEntry.TABLE_1, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    private Uri insertTask(Uri uri, ContentValues contentValues){

        Integer type = contentValues.getAsInteger(TaskEntry.COLUMN_TASK_TYPE);
        if(type==null||!TaskEntry.isValidType(type)){
            throw new IllegalArgumentException("Task requires a type");
        }

        String label = contentValues.getAsString(TaskEntry.COLUMN_TASK_LABEL);
        if (label==null) {
            throw new IllegalArgumentException("Task requires a label");
        }

        Integer time = contentValues.getAsInteger(TaskEntry.COLUMN_NOTIFICATION_TIME);
        if(time==null){
            throw new IllegalArgumentException("Task requires a notification time");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TaskEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return updateTask(uri, contentValues, selection, selectionArgs);
            case TASK_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTask(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateTask(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        if(contentValues.containsKey(TaskEntry.COLUMN_TASK_TYPE)){
            Integer type = contentValues.getAsInteger(TaskEntry.COLUMN_TASK_TYPE);
            if(type==null||!TaskEntry.isValidType(type)){
                throw new IllegalArgumentException("Task requires a type");
            }
        }

        if(contentValues.containsKey(TaskEntry.COLUMN_TASK_LABEL)){
            String label = contentValues.getAsString(TaskEntry.COLUMN_TASK_LABEL);
            if (label==null) {
                throw new IllegalArgumentException("Task requires a label");
            }
        }

        if(contentValues.containsKey(TaskEntry.COLUMN_NOTIFICATION_TIME)){
            Integer time = contentValues.getAsInteger(TaskEntry.COLUMN_NOTIFICATION_TIME);
            if(time==null){
                throw new IllegalArgumentException("Task requires a notification time");
            }
        }

        if(contentValues.size()==0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return database.update(TaskEntry.TABLE_NAME, contentValues,selection,selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return TaskEntry.CONTENT_LIST_TYPE;
            case TASK_ID:
                return TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
