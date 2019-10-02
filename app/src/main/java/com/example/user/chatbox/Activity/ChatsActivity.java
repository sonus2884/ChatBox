package com.example.user.chatbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.chatbox.Adapters.ChatAdapter;
import com.example.user.chatbox.Adapters.RecyclerViewAdapter;
import com.example.user.chatbox.Class.ChatsMsg;
import com.example.user.chatbox.Class.SendReceiveMsg;
import com.example.user.chatbox.Fragments.APIServices;
import com.example.user.chatbox.Fragments.ChatFragment;
import com.example.user.chatbox.Notification.Client;
import com.example.user.chatbox.Notification.Data;
import com.example.user.chatbox.Notification.MyResponse;
import com.example.user.chatbox.Notification.Sender;
import com.example.user.chatbox.Notification.Token;
import com.example.user.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsActivity extends AppCompatActivity {

    private EditText messageText;
    private ImageButton sendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mReferenceSend;
    private DatabaseReference mReferenceReceive;
    private DatabaseReference mSeenRef;
    private DatabaseReference mTypingRef;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<SendReceiveMsg> sendReceiveMessage;
    private SendReceiveMsg sendReceiveMsg;
    private ChatAdapter mAdapter;
    private TextView titleText;
    private CircleImageView chatProfImage;
    private ValueEventListener seenListener;

    // Typing..
    private boolean typingStart;

    // Notification..
    private APIServices apiServices;
    private boolean notify = false;

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
        //https://fcm.googleapis.com/fcm/send

        apiServices = Client.getClient("https://fcm.googleapis.com/").create(APIServices.class);

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

                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {

                    typingStart = true;
                } else if (s.toString().trim().length() == 0 && typingStart) {
                    //Log.i("_Typing", "Typing stop.. ");
                    typingStart = false;

                }
            }
        });

        seenMessage();

    }

    // IS Typing ...

    private void isTyping(final boolean typing) {
        Log.i("_Typing", typingStart + "");
        mTypingRef = FirebaseDatabase.getInstance().getReference("Messages")
                .child(RecyclerViewAdapter.name1 + "_" + ChatFragment.name);

        seenListener = mTypingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ChatsMsg chat = ds.getValue(ChatsMsg.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isTyping", typing);
                    mSeenRef.child(ds.getKey()).updateChildren(hashMap);
                    // Toast.makeText(ChatsActivity.this, "True", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Seen Message..
    private void seenMessage() {

        mSeenRef = FirebaseDatabase.getInstance().getReference("Messages")
                .child(RecyclerViewAdapter.name1 + "_" + ChatFragment.name);

        seenListener = mSeenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ChatsMsg chat = ds.getValue(ChatsMsg.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isSeenMsg", true);
                    mSeenRef.child(ds.getKey()).updateChildren(hashMap);
                    // Toast.makeText(ChatsActivity.this, "True", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void send(View view) {
        notify = true;
        String msg = messageText.getText().toString().trim();

        Date currentTime = Calendar.getInstance().getTime();
        Date dt = new Date(String.valueOf(currentTime));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
        String msgTime = sdf.format(dt);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String msgDate = dateFormat.format(new Date());

        ChatsMsg chatsMsg = new ChatsMsg();

        chatsMsg.setMessage(msg);
        chatsMsg.setUser(ChatFragment.name);
        chatsMsg.setMsgTime(msgTime);
        chatsMsg.setMsgDate(msgDate);
        chatsMsg.setIsSeenMsg(false);

        mReferenceSend.push().setValue(chatsMsg);
        mReferenceReceive.push().setValue(chatsMsg);

        messageText.setText("");

        retrieveMsg();

        //Notification Sending..

        final String message = msg;
        sendNotification(ChatFragment.name, message);
//        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("User details")
//                .child(mAuth.getUid());
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
//                if (notify) {
//                    sendNotification(userDetail.getName(), message);
//                }
//                notify = false;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }

    // Notification sending
    private void sendNotification(final String userName, final String message) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(RecyclerViewAdapter.receiverUid);
        // Log.i("_token",tokens.orderByKey() + "####" + RecyclerViewAdapter.receiverUid +"--"+query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Token token = ds.getValue(Token.class);
                    Data data = new Data(mAuth.getUid(), R.mipmap.ic_launcher, userName + ": " + message,
                            "New Message", RecyclerViewAdapter.receiverUid);

                    Sender sender = new Sender(data, token.getToken());
                    apiServices.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(ChatsActivity.this, " Notification send",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChatsActivity.this, " Notification failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    String msgDate = chatsMsg.getMsgDate();
                    String imageMessage=chatsMsg.getImage_message();
                    boolean isMessageSeen = chatsMsg.getIsSeenMsg();
                    boolean isTyping = chatsMsg.isTyping();
                    //  Log.i("_isSeen", isMessageSeen + "");

                    if (user.equals(ChatFragment.name)) {

                        sendReceiveMsg = new SendReceiveMsg(message, 1, msgTime, isMessageSeen, isTyping, msgDate,imageMessage);
                        sendReceiveMessage.add(sendReceiveMsg);
                        // addMessageBox(message, 1);


                    } else {

                        sendReceiveMsg = new SendReceiveMsg(message, 2, msgTime, isMessageSeen, isTyping, msgDate,imageMessage);
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

    //Image Cropper...
    public void attachedFile(View view) {

        Intent intent=new Intent(ChatsActivity.this,ChatGalleryImgActivity.class);
        startActivity(intent);
        finish();

    }

    private void onOffLine(String onOffLine) {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("User details").child(mAuth.getUid());
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
        mSeenRef.removeEventListener(seenListener);
        onOffLine("offline");
    }

}
