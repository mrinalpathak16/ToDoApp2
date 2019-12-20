package com.example.todoapp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduledFragment extends Fragment{
    RecyclerView view;
    private EditDialogListener listener;
    private RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_name, container, false);

        adapter = new RecyclerViewAdapter(getContext());

        view= rootView.findViewById(R.id.rv);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(getContext()));

        listener = (EditDialogListener)getContext();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registerForContextMenu(this.view);
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



    public interface EditDialogListener{
        void openDialog();
    }

}
