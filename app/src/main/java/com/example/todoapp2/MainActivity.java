package com.example.todoapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todoapp2.data.TaskContract.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//MainActivity which represents the Main Screen of the app
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

        //Setting up tabs using Fragments, ViewPager and TabLayout
        ViewPager viewPager = findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //FloatingActionButton to add new tasks.
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

        if(mCurrentUser!=null) {
            String[] projection = {TaskEntry._ID1};
            String selection = TaskEntry.COLUMN_UID + " = \'" + mCurrentUser.getUid() + "\'";

            Cursor cursor = null;
            try {
                cursor = this.getContentResolver().query(TaskEntry.NAMES_URI, projection, selection,
                        null, null);


            } catch (SQLiteException e) {
                Log.i("mrinal", "No Table");
            }
            if (cursor == null || cursor.getCount() <= 0) {
                ContentValues values = new ContentValues();
                values.put(TaskEntry.COLUMN_UID, mCurrentUser.getUid());
                values.put(TaskEntry.COLUMN_NAME, mCurrentUser.getDisplayName());
                values.put(TaskEntry.COLUMN_EMAIL, mCurrentUser.getEmail());

                Uri uri = this.getContentResolver().insert(TaskEntry.NAMES_URI, values);
            }
        }

        //Receiving email sent by the Notification.
        final String userEmail = getIntent().getStringExtra("Email");

        //Checking if the user is already logged in or not.
        if(mCurrentUser==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);

            //To set Email Id to the edit text if a notification was clicked.
            if(userEmail!=null){
                intent.putExtra("email", userEmail);
                intent.putExtra("verify", "not null");
            }
            startActivity(intent);
            finish();
        }
        else {
            String uid = getIntent().getStringExtra("Uid");

            //if the correct user is already logged in
            if(uid==null||uid.equals(mCurrentUser.getUid())){
                setTitle(mCurrentUser.getDisplayName());
            }

            //To ask for logout if some other user was logged in
            else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Another User Logged In!");
                alertDialogBuilder.setMessage("Logout User : " +'\n'+"Name : "+
                        mCurrentUser.getDisplayName()+'\n'+"Email : "+mCurrentUser.getEmail()+" ?");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("email", userEmail);
                        mAuth.signOut();
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
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


    //to receive calls to open the dialog to edit or add a task
    @Override
    public void openDialog(Uri uri, Cursor cursor){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.setValues(this, uri, cursor, mCurrentUser.getDisplayName(),
                mCurrentUser.getUid(), mCurrentUser.getEmail());
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    //to receive calls to open the dialog to see the details tasks
    @Override
    public void openDetailsDialog(Cursor cursor, Uri uri) {
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.setVal(this, cursor, uri);
        detailsDialog.show(getSupportFragmentManager(), "details dialog");
    }
}
