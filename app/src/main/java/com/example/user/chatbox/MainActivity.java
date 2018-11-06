package com.example.user.chatbox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView cameraView;
    private TextView chatText;
    private TextView statusText;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Animation animation;
    private Animation zoomOut;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private List<UserDetail> details;
    private ProgressBar progressBar;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cameraView = findViewById(R.id.cameraView);
        progressBar = findViewById(R.id.spinner);
        chatText = findViewById(R.id.chatText);
        statusText = findViewById(R.id.statusText);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        details = new ArrayList<UserDetail>();
        init();
        chatbutton();

    }



    private void chatbutton() {

        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        chatText.startAnimation(animation);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        cameraView.startAnimation(zoomOut);
        statusText.setAnimation(zoomOut);
        chatText.setTextColor(Color.WHITE);
        statusText.setTextColor(ContextCompat.getColor(this, R.color.colorLightW));
    }


    private void init() {

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("User details");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                details.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    if (ds.getKey().equals(mAuth.getUid())) {

                        UserDetail dt = ds.getValue(UserDetail.class);
                        name = dt.getName();
                        Log.i("_n", name);
                        continue;
                    }

                    UserDetail userDetail = ds.getValue(UserDetail.class);
                    details.add(userDetail);

                    // Log.i("_details",details.get(0).getName());
                }

                mAdapter = new RecyclerViewAdapter(MainActivity.this, details);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void status(View view) {

        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        statusText.startAnimation(animation);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        cameraView.startAnimation(zoomOut);
        chatText.setAnimation(zoomOut);
        statusText.setTextColor(Color.WHITE);
        chatText.setTextColor(ContextCompat.getColor(this, R.color.colorLightW));

        // status activity..
    }

    public void chats(View view) {

        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        chatText.startAnimation(animation);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        cameraView.startAnimation(zoomOut);
        statusText.setAnimation(zoomOut);
        chatText.setTextColor(Color.WHITE);
        statusText.setTextColor(ContextCompat.getColor(this, R.color.colorLightW));
        // chats activity..
    }

    public void camera(View view) {
        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        cameraView.startAnimation(animation);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        chatText.startAnimation(zoomOut);
        statusText.setAnimation(zoomOut);
        statusText.setTextColor(ContextCompat.getColor(this, R.color.colorLightW));
        chatText.setTextColor(ContextCompat.getColor(this, R.color.colorLightW));


        // camera activity...
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logOut) {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            //onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.setting){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;

        }
        return true;
    }




}
