package com.example.user.chatbox;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextView titleText;
    private CircleImageView chatProfImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);


        messageText = findViewById(R.id.messageText);
        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.sendImage);
        chatProfImage = findViewById(R.id.titleProfImage);
        titleText = findViewById(R.id.titleText);
        titleText.setText(RecyclerViewAdapter.name1);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);


        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        sendReceiveMessage = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mReferenceSend = FirebaseDatabase.getInstance().getReference("Messages")
                .child(ChatFragment.name + "_" + RecyclerViewAdapter.name1);

        mReferenceReceive = FirebaseDatabase.getInstance().getReference("Messages")
                .child(RecyclerViewAdapter.name1 + "_" + ChatFragment.name);


        if (!RecyclerViewAdapter.imageSrc.equals("")) {

            Picasso.with(ChatsActivity.this)
                    .load(RecyclerViewAdapter.imageSrc)
                    .placeholder(R.drawable.ic_account)
                    .fit()
                    .into(chatProfImage);
        }


        retrieveMsg();
        init();

    }

    public void arrowBack(View view) {

        onBackPressed();

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

        Date currentTime = Calendar.getInstance().getTime();
        Date dt = new Date(String.valueOf(currentTime));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
        String msgTime = sdf.format(dt);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String msgDate = dateFormat.format(new Date());

        ChatsMsg chatsMsg = new ChatsMsg();

        chatsMsg.setMessage(msg);
        chatsMsg.setUser(ChatFragment.name);
        chatsMsg.setMsgTime(msgTime);
        chatsMsg.setMsgDate(msgDate);

        mReferenceSend.push().setValue(chatsMsg);
        mReferenceReceive.push().setValue(chatsMsg);

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

                    ChatsMsg chatsMsg = ds.getValue(ChatsMsg.class);

                    String message = chatsMsg.getMessage();
                    String user = chatsMsg.getUser();

                    String msgTime = chatsMsg.getMsgTime();
                    Log.i("_Time", msgTime);

                    if (user.equals(ChatFragment.name)) {

                        sendReceiveMsg = new SendReceiveMsg(message, 1, msgTime);
                        sendReceiveMessage.add(sendReceiveMsg);
                        // addMessageBox(message, 1);


                    } else {

                        sendReceiveMsg = new SendReceiveMsg(message, 2, msgTime);
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

         BottomSheetFragment mBottomSheetFragment = new BottomSheetFragment();
        mBottomSheetFragment.show(getSupportFragmentManager(),mBottomSheetFragment.getTag());


    }


}
