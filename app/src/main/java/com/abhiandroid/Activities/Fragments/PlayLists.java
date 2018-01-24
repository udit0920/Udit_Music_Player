package com.abhiandroid.Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhiandroid.Activities.CreatePlayListDialog;
import com.abhiandroid.Activities.R;

public class PlayLists extends Fragment {

    public PlayLists() {
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
        View inflateView = inflater.inflate(R.layout.fragment_third, container, false);

        FloatingActionButton btnCreatePlayList = (FloatingActionButton) inflateView.findViewById(R.id.fab);
        btnCreatePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatePlayListDialog.class);
                startActivity(intent);
            }
        });


        return inflateView;
    }

}