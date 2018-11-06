package com.example.user.chatbox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<UserDetail> userDetails;
    public static String name1;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;



    public RecyclerViewAdapter(Context context, List<UserDetail> details) {

        this.mContext = context;
        this.userDetails = details;
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

        if (!detail.getAbout().equals(""))
            holder.aboutText.setText(detail.getAbout());

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
               // Intent intent = new Intent(mContext,Camera2Activity.class);
               // mContext.startActivity(intent);
                Toast.makeText(mContext, detail.getName()+"'s Image", Toast.LENGTH_SHORT).show();

            }
        });

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, detail.getName(), Toast.LENGTH_SHORT).show();
                name1 = detail.getName();
                Intent intent = new Intent(mContext,ChatsActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameText;
        TextView aboutText;
        LinearLayout mLinearLayout;
        ImageView expandedImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImageView);
            nameText = itemView.findViewById(R.id.nameText);
            aboutText = itemView.findViewById(R.id.aboutText);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);
            expandedImageView = itemView.findViewById(R.id.expanded_image);
        }
    }

}
