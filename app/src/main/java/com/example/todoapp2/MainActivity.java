package com.example.todoapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
        final String userEmail = getIntent().getStringExtra("Email");
        Log.d("Example Dialog", "Email : "+userEmail);
        if(mCurrentUser==null){
            Log.d("ExampleDialog", "User not signed in");
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            if(userEmail!=null){
                intent.putExtra("email", userEmail);
                intent.putExtra("verify", "not null");
            }
            startActivity(intent);
            finish();
        }
        else {
            String uid = getIntent().getStringExtra("Uid");
            Log.d("ExampleDialog","uid : " +uid);
            if(uid==null||uid.equals(mCurrentUser.getUid())){
                setTitle(mCurrentUser.getDisplayName());
            }
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

    @Override
    public void openDialog(Uri uri, Cursor cursor){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.setValues(this, uri, cursor, mCurrentUser.getDisplayName(),
                mCurrentUser.getUid(), mCurrentUser.getEmail());
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void openDetailsDialog(Cursor cursor, Uri uri) {
        DetailsDialog detailsDialog = new DetailsDialog();
        detailsDialog.setVal(this, cursor, uri);
        detailsDialog.show(getSupportFragmentManager(), "details dialog");
    }
}
