package com.example.user.chatbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.chatbox.Adapters.ViewPagerAdapter;
import com.example.user.chatbox.Fragments.CallFragment;
import com.example.user.chatbox.Fragments.CameraFragment;
import com.example.user.chatbox.Fragments.ChatFragment;
import com.example.user.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  cameraView = findViewById(R.id.cameraView);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new ChatFragment(), "Chat");
        adapter.AddFragment(new CallFragment(), "Call");
        adapter.AddFragment(new CameraFragment(), "Camera");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


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

            Toast.makeText(this, "Working On It!", Toast.LENGTH_SHORT).show();

//            FirebaseAuth.getInstance().signOut();
//
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            //onBackPressed();
            finish();
            return true;
        }

        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;

        }
        return true;
    }

    private void onOffLine(String onOffLine) {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("User details").child(mAuth.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onOffLine", onOffLine);
        mReference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        onOffLine("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        onOffLine("offline");
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        onOffLine("offline");
//    }


}
