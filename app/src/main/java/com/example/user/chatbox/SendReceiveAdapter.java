package com.example.user.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SendReceiveAdapter extends RecyclerView.Adapter<SendReceiveAdapter.MYViewHolder> {

    private Context mContext;
    private List<SendReceive> sendMessage;
    private List<SendReceive> receives;

    public SendReceiveAdapter(Context context, List<SendReceive> message, List<SendReceive> receives) {

        this.mContext = context;
        this.sendMessage = message;
        this.receives = receives;
        // Log.i("_msg",sendMessage);
    }


    @NonNull
    @Override
    public SendReceiveAdapter.MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.send_recive_sms, parent, false);

        return new MYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendReceiveAdapter.MYViewHolder holder, int position) {

        if (sendMessage.size() > position) {

            SendReceive sendReceive = sendMessage.get(position);
            holder.receiveText.setVisibility(View.VISIBLE);
            // holder.receiveText.setVisibility(View.INVISIBLE);
            holder.receiveText.setText(sendReceive.getSendSms());

           // RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
             //       ViewGroup.LayoutParams.WRAP_CONTENT);
            //  params1.addRule(RelativeLayout.BELOW, R.id.receiveText);
           // params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
          //  holder.sendText.setLayoutParams(params1);

        }

        if (receives.size() > position) {

            SendReceive receive = receives.get(position);
            holder.sendText.setVisibility(View.VISIBLE);
            //  holder.receiveText.setVisibility(View.INVISIBLE);
            holder.sendText.setText(receive.getSendSms());

            //RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                 //   ViewGroup.LayoutParams.WRAP_CONTENT);
            // params2.addRule(RelativeLayout.BELOW, R.id.sendText);
         //   params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
           // holder.receiveText.setLayoutParams(params2);


            //holder.receiveText.setGravity(Gravity.START | Gravity.LEFT);


        }

    }

    @Override
    public int getItemCount() {

        int sendSize = sendMessage.size();
        int recevSize = receives.size();
        if (recevSize > sendSize) {

            sendSize = recevSize;
        }
        return sendSize;
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        TextView sendText;
        TextView receiveText;

        public MYViewHolder(View itemView) {
            super(itemView);

            sendText = itemView.findViewById(R.id.sendText);
            receiveText = itemView.findViewById(R.id.receiveText);
        }
    }
}
