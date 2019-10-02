package com.example.user.chatbox.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.chatbox.Activity.ChatsActivity;
import com.example.user.chatbox.Activity.ProfileActivity;
import com.example.user.chatbox.Class.SendReceiveMsg;
import com.example.user.chatbox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_HEADER = 3;
    private List<SendReceiveMsg> message;
    private Context mContext;

    public ChatAdapter(Context context, List<SendReceiveMsg> message) {

         mContext = context;
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
                ((SendMessageHolder) holder).bind(msg, position, message,mContext);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMessageHolder) holder).bind(msg, position, message,mContext);
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
    private ImageView send_view_msg;
    private LinearLayout messageLayout;
    private RelativeLayout imageMsgLayout;

    //Image..
    private ImageView send_image;
    private ImageView send_view_img;
    private TextView send_img_time;


    SendMessageHolder(View itemView) {
        super(itemView);

        sendText = itemView.findViewById(R.id.sendText);
        sendMsgTime = itemView.findViewById(R.id.sendMsgTime);
        //seen_msg = itemView.findViewById(R.id.text_seen);
        send_view_msg = itemView.findViewById(R.id.send_view_msg);
        header = itemView.findViewById(R.id.header);
        date_tv = itemView.findViewById(R.id.date_tv);
        //ImageView
        send_image=itemView.findViewById(R.id.send_image);
        send_view_img= itemView.findViewById(R.id.send_view_msg_img);
        send_img_time=itemView.findViewById(R.id.send_img_time);

        messageLayout=itemView.findViewById(R.id.r1);
        imageMsgLayout=itemView.findViewById(R.id.r1_image_layout);


    }

    void bind(SendReceiveMsg message, int p, List<SendReceiveMsg> msg,Context mContext) {

        if (message.getMessage() != null) {
            messageLayout.setVisibility(View.VISIBLE);
            imageMsgLayout.setVisibility(View.GONE);

            sendText.setText(message.getMessage());
            sendMsgTime.setText(message.getMsgTime());
            if (p >= 0) {
                if (message.isSeenMsg()) {
                    // seen_msg.setText("seen");
                    send_view_msg.setImageResource(R.drawable.ic_seen_msg);
                } else {
                    // seen_msg.setText("deliver");
                    send_view_msg.setImageResource(R.drawable.ic_send_message);
                }
            } else {
                seen_msg.setVisibility(View.GONE);
                send_view_msg.setVisibility(View.GONE);

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

        //Send Image View..
        }else if(message.getImage_message() !=null){
            messageLayout.setVisibility(View.GONE);
            imageMsgLayout.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(message.getImage_message())
                    .placeholder(R.drawable.ic_account)
                    .fit()
                    .into(send_image);
            send_img_time.setText(message.getMsgTime());

            if (p >= 0) {
                if (message.isSeenMsg()) {
                    // seen_msg.setText("seen");
                    send_view_img.setImageResource(R.drawable.ic_seen_msg);
                } else {
                    // seen_msg.setText("deliver");
                    send_view_img.setImageResource(R.drawable.ic_send_message);
                }
            } else {
                seen_msg.setVisibility(View.GONE);
                send_view_img.setVisibility(View.GONE);

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
}

class ReceiveMessageHolder extends RecyclerView.ViewHolder {

    private TextView receiveText;
    private TextView receiveMsgTime;
    private LinearLayout header;
    private TextView date_tv;

    //Receive Image..
    private ImageView receive_image;
    private TextView receive_img_time;
    private RelativeLayout receive_msg_layout;
    private RelativeLayout receive_msg_img_layout;


    ReceiveMessageHolder(View itemView) {
        super(itemView);

        receiveText = itemView.findViewById(R.id.receiveText);
        receiveMsgTime = itemView.findViewById(R.id.receiveMsgTime);
        header = itemView.findViewById(R.id.header);
        date_tv = itemView.findViewById(R.id.date_tv);
        // Receive Image View...
        receive_image=itemView.findViewById(R.id.receive_image);
        receive_img_time=itemView.findViewById(R.id.receive_img_time);
        receive_msg_layout=itemView.findViewById(R.id.receive_msg_layout);
        receive_msg_img_layout=itemView.findViewById(R.id.receive_image_layout);
    }


    void bind(SendReceiveMsg message, int position, List<SendReceiveMsg> msg,Context mContext) {
        if (message.getMessage() != null) {
            receive_msg_img_layout.setVisibility(View.GONE);
            receive_msg_layout.setVisibility(View.VISIBLE);
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
        //Receive Image View...
        else if (message.getImage_message() !=null){
            receive_msg_img_layout.setVisibility(View.VISIBLE);
            receive_msg_layout.setVisibility(View.GONE);

            Picasso.with(mContext)
                    .load(message.getImage_message())
                    .placeholder(R.drawable.ic_account)
                    .fit()
                    .into(receive_image);

            receive_img_time.setText(message.getMsgTime());

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

}



