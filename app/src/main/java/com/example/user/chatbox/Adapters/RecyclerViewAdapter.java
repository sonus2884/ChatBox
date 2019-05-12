package com.example.user.chatbox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.chatbox.Activity.ChatsActivity;
import com.example.user.chatbox.Class.ChatsMsg;
import com.example.user.chatbox.Class.UserDetail;
import com.example.user.chatbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public static String name1;
    public static String imageSrc;
    private String theLastMessage;
    private String theLastMsgDate;
    private Context mContext;
    private List<UserDetail> userDetails;
    private boolean isOnline;

    public RecyclerViewAdapter(Context context, List<UserDetail> details, boolean isOnline) {

        this.mContext = context;
        this.userDetails = details;
        this.isOnline = isOnline;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final UserDetail detail = userDetails.get(position);
        // Log.i("_d", "Hello");
        holder.nameText.setText(detail.getName());

        if (!detail.getImageUri().equals("")) {

            Picasso.with(mContext)
                    .load(detail.getImageUri())
                    .placeholder(R.drawable.ic_account)
                    .fit()
                    .into(holder.imageView);

        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mContext, detail.getName() + "'s Image", Toast.LENGTH_SHORT).show();

            }
        });

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mContext, detail.getName(), Toast.LENGTH_SHORT).show();
                name1 = detail.getName();

                imageSrc = detail.getImageUri();

                Intent intent = new Intent(mContext, ChatsActivity.class);
                mContext.startActivity(intent);


            }
        });

        if (isOnline) {
            LastMessage(detail.getName(), holder.last_msg,holder.msg_last_date);
        }

        if (isOnline) {
            if (detail.getOnOffLine() != null && detail.getOnOffLine().equalsIgnoreCase("online")) {
                holder.img_off.setVisibility(View.GONE);
                holder.img_on.setVisibility(View.VISIBLE);
            } else {
                holder.img_off.setVisibility(View.VISIBLE);
                holder.img_on.setVisibility(View.GONE);
            }
        } else {
            holder.img_off.setVisibility(View.GONE);
            holder.img_on.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    private void LastMessage(final String userId, final TextView last_msg, final TextView last_msg_date) {

        final String[] userName = new String[1];

        theLastMessage = "default";

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference()
                .child("User details").child(firebaseUser.getUid());

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserDetail detail = dataSnapshot.getValue(UserDetail.class);
                userName[0] = detail.getName();
              //  Log.i("_ku", userName[0] + "_" + userId);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Messages")
                        .child(userName[0] + "_" + userId);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            ChatsMsg chat = ds.getValue(ChatsMsg.class);
                            Log.i("_key", chat.getMessage());

                                theLastMessage = chat.getMessage();
                                theLastMsgDate = chat.getMsgDate();
                        }

                        switch (theLastMessage) {
                            case "default":
                                last_msg.setText("");
                                last_msg_date.setText("");
                                break;
                            default:
                                last_msg.setText(theLastMessage);
                                last_msg_date.setText(theLastMsgDate);
                                break;
                        }

                        theLastMessage = "default";

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    // Checking for last message..

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameText;
        TextView last_msg;
        RelativeLayout mLinearLayout;
        ImageView img_on;
        ImageView img_off;
        TextView msg_last_date;


        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImageView);
            nameText = itemView.findViewById(R.id.nameText);
            last_msg = itemView.findViewById(R.id.last_msg);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            msg_last_date = itemView.findViewById(R.id.msg_dateText);
        }
    }

}
