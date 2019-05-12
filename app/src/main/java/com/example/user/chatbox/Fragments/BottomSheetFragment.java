package com.example.user.chatbox.Fragments;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.chatbox.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private LinearLayout cameraLayout;
    private LinearLayout galleryLayout;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        cameraLayout = view.findViewById(R.id.cameraLayout);
        galleryLayout = view.findViewById(R.id.galleryLayout);

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Gallery", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}