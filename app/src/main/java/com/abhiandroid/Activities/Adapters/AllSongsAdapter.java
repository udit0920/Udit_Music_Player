package com.abhiandroid.Activities.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.AudioModel;
import com.abhiandroid.Activities.R;

import java.util.List;

import com.abhiandroid.Activities.Services.MusicService;

/**
 * Created by uditsetia on 26/12/17.
 */
public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.MyViewHolder> {

    private static final String TAG = "AllSongsAdapter";
    BroadcastReceiver receiver;
    List<AudioModel> songsList;
    Context context;
    int btnState;
    MyViewHolder holder;
    Intent intent;

    public AllSongsAdapter(List<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_songs_layout, parent, false);
        setupBroadCast();
        return new MyViewHolder(view);
    }

    private void setupBroadCast() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                btnState = intent.getIntExtra("btnState", 0);
                int position = intent.getIntExtra("position", 0);
                Intent intent2 = new Intent(context, MusicService.class);
                if (position + 1 >= songsList.size()) {
                    position = -1;
                }
                intent2.putExtra("file_path", songsList.get(position + 1).getaPath());
                intent2.putExtra("position", position + 1);
                context.startService(intent2);

                for (int i = 0; i < songsList.size(); i++) {
                    if (i != position + 1) {
                        songsList.get(i).setButtonState(0);
                    }else{
                        songsList.get(i).setButtonState(1);
                    }
                }
                AllSongsAdapter.this.notifyDataSetChanged();

            }
        };

        context.registerReceiver(receiver, new IntentFilter("udit.ButtonState"));

    }


    public void myOnDestroy() {
        context.unregisterReceiver(receiver);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        int songDuration = Integer.valueOf(songsList.get(position).getDuration());
        holder.songName.setText(songsList.get(position).getaName());
        holder.singerName.setText(songsList.get(position).getaArtist());
        holder.songDuration.setText(getProperDuration(songDuration));
        final String filePath = songsList.get(position).getaPath();

        final int btnState = songsList.get(position).getButtonState();

        if (btnState == 1) {
            holder.btnPlay.setVisibility(View.GONE);
            holder.btnPause.setVisibility(View.VISIBLE);
        } else {
            holder.btnPause.setVisibility(View.GONE);
            holder.btnPlay.setVisibility(View.VISIBLE);
        }


        holder.musicRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnState == 0) {
                    songsList.get(position).setButtonState(1);
                    for (int i = 0; i < songsList.size(); i++) {
                        if (i != position) {
                            songsList.get(i).setButtonState(0);
                        }
                    }
                    playSong(filePath, position);
                    AllSongsAdapter.this.notifyDataSetChanged();
                }else{
                    songsList.get(position).setButtonState(0);
                    stopSong(position);
                }
            }
        });

    }

    private void stopSong(int position) {

        MusicService.mediaPlayer.stop();
        MusicService.mediaPlayer.reset();
        this.notifyItemChanged(position);

    }

    private String getProperDuration(int songDuration) {

        int seconds = songDuration / 1000;
        int minutes = seconds / 60;
        int remainingSeconds = seconds - minutes * 60;
        String duration = minutes + ":" + remainingSeconds;
        if (remainingSeconds < 10) {
            duration = minutes + ":0" + remainingSeconds;
        }
        return duration;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + songsList.size());
        return songsList.size();
    }


    private void playSong(String filePath, int position) {
        intent = new Intent(context, MusicService.class);
        intent.putExtra("file_path", filePath);
        intent.putExtra("position", position);
        context.startService(intent);
        updateNowPlaying(position);

    }


    private void updateNowPlaying(int position) {

        Intent intent = new Intent("udit.nowPlaying");
        intent.putExtra("songName", songsList.get(position).getaName());
        intent.putExtra("singer", songsList.get(position).getaArtist());
        intent.putExtra("filePath", songsList.get(position).getaPath());
        context.sendBroadcast(intent);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout musicRow;
        TextView songName, singerName, songDuration;
        ImageView btnPause, btnPlay;

        public MyViewHolder(View itemView) {
            super(itemView);
            musicRow = (RelativeLayout) itemView.findViewById(R.id.music_row);
            btnPause = (ImageView) itemView.findViewById(R.id.btn_pause);
            btnPlay = (ImageView) itemView.findViewById(R.id.btn_play);
            songName = (TextView) itemView.findViewById(R.id.tv_song_name);
            singerName = (TextView) itemView.findViewById(R.id.tv_singer_name);
            songDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }

    }


}
