package com.example.user.chatbox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.chatbox.R;
import com.example.user.chatbox.Adapters.RecyclerViewAdapter;
import com.example.user.chatbox.Class.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private List<UserDetail> details;

    private ProgressBar progressBar;

    public static String name;

    private View view;

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.chat_fragement, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressBar = view.findViewById(R.id.spinner);
        progressBar.setVisibility(View.VISIBLE);
        details = new ArrayList<UserDetail>();


        init();

        return view;
    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("User details");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                details.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    if (ds.getKey().equals(mAuth.getUid())) {

                        UserDetail dt = ds.getValue(UserDetail.class);
                        name = dt.getName();
                        Log.i("_n", name);
                        continue;
                    }

                    UserDetail userDetail = ds.getValue(UserDetail.class);
                    details.add(userDetail);

                }

                mAdapter = new RecyclerViewAdapter(getContext(), details,true);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
