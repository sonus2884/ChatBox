package com.example.user.chatbox.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.chatbox.Class.SendReceiveMsg;
import com.example.user.chatbox.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_HEADER = 3;
    private List<SendReceiveMsg> message;

    public ChatAdapter(Context context, List<SendReceiveMsg> message) {

        Context mContext = context;
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
        RecyclerView.ViewHolder holder1 = holder;
        int position1 = position;

        SendReceiveMsg msg = message.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageHolder) holder).bind(msg, position, message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMessageHolder) holder).bind(msg, position, message);
        }
    }

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
    private LinearLayout header;
    private TextView date_tv;


    SendMessageHolder(View itemView) {
        super(itemView);

        sendText = itemView.findViewById(R.id.sendText);
        sendMsgTime = itemView.findViewById(R.id.sendMsgTime);
        seen_msg = itemView.findViewById(R.id.text_seen);

        header = itemView.findViewById(R.id.header);
        date_tv = itemView.findViewById(R.id.date_tv);


    }

    void bind(SendReceiveMsg message, int p, List<SendReceiveMsg> msg) {

        sendText.setText(message.getMessage());
        sendMsgTime.setText(message.getMsgTime());
        if (p == msg.size() - 1) {
            if (message.isSeenMsg()) {
                seen_msg.setText("seen");
            } else {
                seen_msg.setText("deliver");
            }
        } else {
            seen_msg.setVisibility(View.GONE);
        }

        date_tv.setText(message.getMsgDate());
        if (p > 0) {
            if (msg.get(p).getMsgDate().equalsIgnoreCase(msg.get(p - 1).getMsgDate())) {
                header.setVisibility(View.GONE);
            } else {
                header.setVisibility(View.VISIBLE);
            }
        } else {
            header.setVisibility(View.VISIBLE);
        }


    }
}

class ReceiveMessageHolder extends RecyclerView.ViewHolder {

    private TextView receiveText;
    private TextView receiveMsgTime;
    private LinearLayout header;
    private TextView date_tv;

    ReceiveMessageHolder(View itemView) {
        super(itemView);

        receiveText = itemView.findViewById(R.id.receiveText);
        receiveMsgTime = itemView.findViewById(R.id.receiveMsgTime);
        header = itemView.findViewById(R.id.header);
        date_tv = itemView.findViewById(R.id.date_tv);
    }


    void bind(SendReceiveMsg message, int position, List<SendReceiveMsg> msg) {

        receiveText.setText(message.getMessage());
        receiveMsgTime.setText(message.getMsgTime());
        date_tv.setText(message.getMsgDate());
        if (position > 0) {
            if (msg.get(position).getMsgDate().equalsIgnoreCase(msg.get(position - 1).getMsgDate())) {
                header.setVisibility(View.GONE);
            } else {
                header.setVisibility(View.VISIBLE);
            }
        } else {
            header.setVisibility(View.VISIBLE);
        }
    }

}



