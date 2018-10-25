package com.example.user.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SendReceiveMsg> message;

    public ChatAdapter(Context context, List<SendReceiveMsg> message) {

        this.mContext = context;
        this.message = message;

    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {


        SendReceiveMsg msg = message.get(position);

        if (msg.getId() == 1) {

            holder.sendText.setVisibility(View.VISIBLE);
            holder.sendText.setText(msg.getMessage());


        } else {

            holder.receiveText.setVisibility(View.VISIBLE);
            holder.receiveText.setText(msg.getMessage());

        }
        // Log.i("_Message",msg.getMessage());


    }

    @Override
    public int getItemCount() {


        return message.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sendText;
        TextView receiveText;

        public MyViewHolder(View itemView) {
            super(itemView);

            sendText = itemView.findViewById(R.id.sendText);
            receiveText = itemView.findViewById(R.id.receiveText);
        }
    }
}
