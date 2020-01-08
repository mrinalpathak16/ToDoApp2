package com.example.todoapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.example.todoapp2.data.TaskContract.TaskEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyListCursorAdapter.RecyclerClickListener,
        OngoingFragment.EditDialogListener,
        ScheduledFragment.EditDialogListener,
        CompletedFragment.EditDialogListener{
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ViewPager viewPager = findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(null, null);
                Toast.makeText(MainActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser==null){
            Log.i("Login", "User not signed in");
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openDialog(Uri uri, Cursor cursor){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.setValues(this, uri, cursor, mCurrentUser.getEmail(), mCurrentUser.getUid());
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void openDetailsDialog(Cursor cursor, Uri uri) {
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.setVal(this, cursor, uri);
        detailsDialog.show(getSupportFragmentManager(), "details dialog");
    }
}
