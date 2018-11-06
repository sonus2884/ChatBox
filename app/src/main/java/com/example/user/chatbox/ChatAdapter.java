package com.example.user.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SendReceiveMsg> message;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private RecyclerView.ViewHolder holder;
    private int position;

     ChatAdapter(Context context, List<SendReceiveMsg> message) {

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.holder = holder;
        this.position = position;


        SendReceiveMsg msg = message.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageHolder) holder).bind(msg);
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

    TextView sendText;


    SendMessageHolder(View itemView) {
        super(itemView);

        sendText = itemView.findViewById(R.id.sendText);


    }

    void bind(SendReceiveMsg message) {

        sendText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        // timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
    }
}

class ReceiveMessageHolder extends RecyclerView.ViewHolder {

    TextView receiveText;

    ReceiveMessageHolder(View itemView) {
        super(itemView);

        receiveText = itemView.findViewById(R.id.receiveText);
    }


    void bind(SendReceiveMsg message) {

        receiveText.setText(message.getMessage());
    }

}

