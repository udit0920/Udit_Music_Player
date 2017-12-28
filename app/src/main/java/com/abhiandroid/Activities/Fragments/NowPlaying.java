package com.abhiandroid.Activities.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.R;

public class NowPlaying extends Fragment {
    String filePath;
    String singer;
    String songName;
    View view;
    BroadcastReceiver receiver;
    static TextView tvSongName,tvSinger;
    public NowPlaying() {
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
        view =  inflater.inflate(R.layout.fragment_second, container, false);
        tvSongName = (TextView)view.findViewById(R.id.tv_song_name);
        tvSinger = (TextView)view.findViewById(R.id.tv_singer);
        setupBroadCast();
        return view;
    }

    private void setupBroadCast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LayoutInflater inflater = getLayoutInflater(null);

                filePath = intent.getStringExtra("filePath");
                singer = intent.getStringExtra("singer");
                songName = intent.getStringExtra("songName");
                Toast.makeText(getActivity(),songName, Toast.LENGTH_LONG).show();
                tvSinger.setText(singer);
                tvSongName.setText(songName);
            }
        };

        getActivity().registerReceiver(receiver, new IntentFilter("udit.nowPlaying"));

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);

    }


}