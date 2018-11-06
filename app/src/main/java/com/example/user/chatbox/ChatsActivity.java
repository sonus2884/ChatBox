package com.example.user.chatbox;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    private EditText messageText;
    private ImageButton sendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mReferenceSend;
    private DatabaseReference mReferenceReceive;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SendReceiveMsg> sendReceiveMessage;
    private SendReceiveMsg sendReceiveMsg;
    private ChatAdapter mAdapter;

   // private Map map;
    private GenericTypeIndicator<Map<String, String>> genericTypeIndicator;
    // private LinearLayout layout;
    // private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // layout = findViewById(R.id.layout1);
        // scrollView = findViewById(R.id.scrollView);
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

       // map = new HashMap();
        genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
        };

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        sendReceiveMessage = new ArrayList<>();

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

        Map<String, String> map = new HashMap<>();
        map.put("message", msg);
        map.put("user", MainActivity.name);

        mReferenceSend.push().setValue(map);
        mReferenceReceive.push().setValue(map);
        messageText.setText("");

        retrieveMsg();

    }


    private void retrieveMsg() {


        mReferenceSend.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sendReceiveMessage.clear();
                //map.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                   Map map = ds.getValue(genericTypeIndicator);

                    String message = map.get("message").toString();
                    String user = map.get("user").toString();


                    if (user.equals(MainActivity.name)) {

                        sendReceiveMsg = new SendReceiveMsg(message, 1);
                        sendReceiveMessage.add(sendReceiveMsg);
                        // addMessageBox(message, 1);


                    } else{

                        sendReceiveMsg = new SendReceiveMsg(message, 2);
                        sendReceiveMessage.add(sendReceiveMsg);
                        // addMessageBox(message, 2);
                    }

                }


                mAdapter = new ChatAdapter(ChatsActivity.this, sendReceiveMessage);
                recyclerView.setAdapter(mAdapter);
                recyclerView.smoothScrollToPosition(mAdapter.getItemCount());





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void attachedFile(View view) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
    }

    public void cameraOpen(View view) {

        Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
    }

    public void galleryOpen(View view) {


        Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
    }
}
