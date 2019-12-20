package com.example.todoapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.RecyclerClickListener,
        OngoingFragment.EditDialogListener, ScheduledFragment.EditDialogListener,
        CompletedFragment.EditDialogListener {

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
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void openDetailsDialog() {
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.setVal(this);
        detailsDialog.show(getSupportFragmentManager(), "details dialog");
    }
}
