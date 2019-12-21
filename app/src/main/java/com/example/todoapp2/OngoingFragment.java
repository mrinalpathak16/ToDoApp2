package com.example.todoapp2;

import android.database.Cursor;
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

public class OngoingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView mRecyclerView;
    private EditDialogListener listener;
    private MyListCursorAdapter mAdapter;
    private static final int TASK_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_name, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv);

        mAdapter = new MyListCursorAdapter(getContext(),null);
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
        switch (item.getItemId()) {
            case R.id.editMenu:
                // do your stuff
                listener.openDialog();
                break;
            case R.id.deleteMenu:
                break;
        }
        return super.onContextItemSelected(item);
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

        return new CursorLoader(getContext(),
                TaskEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

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
        void openDialog();
    }

}
