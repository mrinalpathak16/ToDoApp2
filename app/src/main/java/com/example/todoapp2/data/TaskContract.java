package com.example.todoapp2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.todoapp2";

    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TASKS = "tasks";

    public static final String PATH_NAMES = "names";

    public static class TaskEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_TASKS);

        public static final Uri NAMES_URI = Uri.withAppendedPath(BASE_URI, PATH_NAMES);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        public static final String TABLE_NAME = "tasks";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TASK_TYPE = "type";
        public static final String COLUMN_TASK_LABEL = "label";
        public static final String COLUMN_TASK_DESCRIPTION = "description";
        public static final String COLUMN_NOTIFICATION_TIME = "time";
        public static final String COLUMN_USER_ID = "user";
        public static final String COLUMN_TASK_STATUS = "status";

        public static final String TABLE_1 = "names";
        public static final String _ID1 = BaseColumns._ID;
        public static final String COLUMN_UID = "uid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";

        public static final int NORMAL_TASK = 0;
        public static final int PRIORITY_TASK = 1;

        public static final int SCHEDULED_TASK = 0;
        public static final int ONGOING_TASK = 1;
        public static final int COMPLETED_TASK = 2;

        public static boolean isValidType(int type){
            if(type==NORMAL_TASK||type==PRIORITY_TASK){
                return true;
            }
            return false;
        }
    }
}
