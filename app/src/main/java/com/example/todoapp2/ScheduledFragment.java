package com.example.todoapp2;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp2.data.TaskContract.TaskEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScheduledFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView mRecyclerView;
    private EditDialogListener listener;
    private MyListCursorAdapter mAdapter;
    private static final int TASK_LOADER = 0;
    public Cursor mCursor;
    public FirebaseAuth mAuth;
    public FirebaseUser mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_name, container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mRecyclerView = rootView.findViewById(R.id.rv);

        mAdapter = new MyListCursorAdapter(getContext(),null,102);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listener = (EditDialogListener)getContext();

        getLoaderManager().initLoader(TASK_LOADER,null, this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registerForContextMenu(this.mRecyclerView);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mCursor = mAdapter.getCursor();
        Uri uri = Uri.withAppendedPath(TaskEntry.CONTENT_URI,
                String.valueOf(mAdapter.getItemId(mCursor.getPosition())));
        if(getUserVisibleHint()&&item.getGroupId()==102) {
            switch (item.getItemId()) {
                case R.id.editMenu:
                    // do your stuff
                    listener.openDialog(uri, mCursor);
                    break;
                case R.id.deleteMenu:
                    getContext().getContentResolver().delete(uri, null, null);
                    int Id = Integer.parseInt(uri.getLastPathSegment());
                    ExampleDialog dialog1 = new ExampleDialog();
                    dialog1.cancelAlarm(Id, getContext());
                    break;
            }
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_TASK_TYPE,
                TaskEntry.COLUMN_TASK_LABEL,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskEntry.COLUMN_NOTIFICATION_TIME
        };

        String selection = TaskEntry.COLUMN_USER_ID + "=\'" + mCurrentUser.getUid() +
                "\' AND " + TaskEntry.COLUMN_TASK_STATUS + "=\'" + TaskEntry.SCHEDULED_TASK + '\'';

        return new CursorLoader(getContext(),
                TaskEntry.CONTENT_URI,
                projection,
                selection,
                null,
                TaskEntry.COLUMN_NOTIFICATION_TIME + " ASC");

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    public interface EditDialogListener{
        void openDialog(Uri uri, Cursor cursor);
    }

}
