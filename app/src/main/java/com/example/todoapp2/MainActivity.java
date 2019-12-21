package com.example.todoapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.example.todoapp2.data.TaskContract.TaskEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyListCursorAdapter.RecyclerClickListener,
        OngoingFragment.EditDialogListener,
        ScheduledFragment.EditDialogListener,
        CompletedFragment.EditDialogListener {
    private long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                Toast.makeText(MainActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog(null,this);
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void openDetailsDialog(Cursor cursor, Uri uri) {
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.setVal(this, cursor, uri);
        detailsDialog.show(getSupportFragmentManager(), "details dialog");
    }



}
