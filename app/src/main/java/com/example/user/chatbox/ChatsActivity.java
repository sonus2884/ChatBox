package com.example.user.chatbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private EditText messageText;
   private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lm;
    private RecyclerView.Adapter mAdapter;
    private List<String> message;
    private ImageButton sendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mReferenceSend;
    private DatabaseReference mReferenceReceive;

    private List<SendReceive> msg;
    private List<SendReceive> receives;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        messageText = findViewById(R.id.messageText);
       recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.sendImage);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(RecyclerViewAdapter.name1);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        msg = new ArrayList<>();
        receives = new ArrayList<>();

        lm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lm);
        message = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mReferenceSend = FirebaseDatabase.getInstance().getReference("Messages")
                .child(MainActivity.name + "_" + RecyclerViewAdapter.name1);

        mReferenceReceive = FirebaseDatabase.getInstance().getReference("Messages")
                .child(RecyclerViewAdapter.name1 + "_" + MainActivity.name);

        retrieveMsg();
        init();

    }


    private void init() {

        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String ch = s.toString().trim();
                if (ch.equals("")) {

                    sendButton.setVisibility(View.INVISIBLE);

                } else {

                    sendButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void send(View view) {
        String msg = messageText.getText().toString().trim();

        message.add(msg);

        SendReceive sendReceive = new SendReceive();
        sendReceive.setSendSms(msg);
        mReferenceSend.push().setValue(sendReceive);
        //  mReferenceReceive.push().setValue(sendReceive);
        messageText.setText("");
        retrieveMsg();

    }


    private void retrieveMsg() {


        mReferenceSend.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msg.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    SendReceive sendReceive = ds.getValue(SendReceive.class);
                    msg.add(sendReceive);

                }
                mAdapter = new SendReceiveAdapter(ChatsActivity.this, msg, receives);
               recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mReferenceReceive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receives.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    SendReceive receive = ds.getValue(SendReceive.class);
                    receives.add(receive);


                }

                mAdapter = new SendReceiveAdapter(ChatsActivity.this, msg, receives);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
