package com.example.user.chatbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.chatbox.Camera2Activity;
import com.example.user.chatbox.Fragments.ChatFragment;
import com.example.user.chatbox.R;
import com.example.user.chatbox.Class.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView aboutUs;
    private CircleImageView profileImage;
    private TextView nameText;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Setting");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        aboutUs = findViewById(R.id.about);
        profileImage = findViewById(R.id.profile_image);
        nameText = findViewById(R.id.nameText);

        nameText.setText(ChatFragment.name);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("User details");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    if (ds.getKey().equals(mAuth.getUid())) {

                        UserDetail dt = ds.getValue(UserDetail.class);


                        if (!dt.getImageUri().equals("")) {

                            Picasso.with(ProfileActivity.this)
                                    .load(dt.getImageUri())
                                    .placeholder(R.drawable.ic_account)
                                    .fit()
                                    .into(profileImage);

                        }

                        continue;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editName(View view) {


    }

    public void aboutUs(View view) {

        Intent intent = new Intent(ProfileActivity.this, AboutUsActivity.class);
        startActivity(intent);

    }

    public void editImage(View view) {

        Intent intent = new Intent(ProfileActivity.this, Camera2Activity.class);
        startActivity(intent);
        finish();
    }
}
