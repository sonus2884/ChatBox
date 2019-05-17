package com.example.user.chatbox.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.chatbox.Class.SendReceiveMsg;
import com.example.user.chatbox.Notification.Token;
import com.example.user.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context mContext;
    private List<SendReceiveMsg> message;
    private RecyclerView.ViewHolder holder;
    private int position;

    public ChatAdapter(Context context, List<SendReceiveMsg> message) {

        this.mContext = context;
        this.message = message;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_msg, parent, false);
            return new SendMessageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receive_msg, parent, false);
            return new ReceiveMessageHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        this.holder = holder;
        this.position = position;


        SendReceiveMsg msg = message.get(position);


        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageHolder) holder).bind(msg, position, message.size() - 1);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMessageHolder) holder).bind(msg);
        }
    }
    // Log.i("_Message",msg.getMessage());


    @Override
    public int getItemCount() {


        return message.size();
    }

    @Override
    public int getItemViewType(int position) {

        SendReceiveMsg m = message.get(position);

        if (m.getId() == 1) {

            return VIEW_TYPE_MESSAGE_SENT;

        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }
}


class SendMessageHolder extends RecyclerView.ViewHolder {

    private TextView sendText;
    private TextView sendMsgTime;
    private TextView seen_msg;


    SendMessageHolder(View itemView) {
        super(itemView);

        sendText = itemView.findViewById(R.id.sendText);
        sendMsgTime = itemView.findViewById(R.id.sendMsgTime);
        seen_msg = itemView.findViewById(R.id.text_seen);


    }

    void bind(SendReceiveMsg message, int p, int m) {

        sendText.setText(message.getMessage());
        sendMsgTime.setText(message.getMsgTime());
        if (p == m) {
            if (message.isSeenMsg()) {
                seen_msg.setText("seen");
            } else {
                seen_msg.setText("deliver");
            }
        } else {
            seen_msg.setVisibility(View.GONE);
        }


    }
}

class ReceiveMessageHolder extends RecyclerView.ViewHolder {

    private TextView receiveText;
    private TextView receiveMsgTime;

    ReceiveMessageHolder(View itemView) {
        super(itemView);

        receiveText = itemView.findViewById(R.id.receiveText);
        receiveMsgTime = itemView.findViewById(R.id.receiveMsgTime);
    }


    void bind(SendReceiveMsg message) {

        receiveText.setText(message.getMessage());
        receiveMsgTime.setText(message.getMsgTime());

    }



}

