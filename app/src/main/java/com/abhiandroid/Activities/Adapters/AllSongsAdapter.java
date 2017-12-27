package com.abhiandroid.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.AudioModel;
import com.abhiandroid.Activities.R;

import java.util.List;

import com.abhiandroid.Activities.Services.MusicService;

/**
 * Created by uditsetia on 26/12/17.
 */
public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.MyViewHolder>{

    private static final String TAG = "AllSongsAdapter";
    List<AudioModel> songsList;
    Context context;
    MyViewHolder holder;

    public AllSongsAdapter(List<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_songs_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        int songDuration = Integer.valueOf(songsList.get(position).getDuration());
        holder.songName.setText(songsList.get(position).getaName());
        holder.singerName.setText(songsList.get(position).getaArtist());
        holder.songDuration.setText(getProperDuration(songDuration));
        final String filePath = songsList.get(position).getaPath();

        int btnState = songsList.get(position).getButtonState();

        if(btnState==1){
            holder.btnPlay.setVisibility(View.GONE);
            holder.btnPause.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnPause.setVisibility(View.GONE);
            holder.btnPlay.setVisibility(View.VISIBLE);
        }


        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsList.get(position).setButtonState(1);
                for(int i=0;i<songsList.size();i++){
                    if(i!=position){
                        songsList.get(i).setButtonState(0);
                    }
                }
                playSong(filePath);
                AllSongsAdapter.this.notifyDataSetChanged();
            }
        });
        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsList.get(position).setButtonState(0);
                stopSong();
            }
        });
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



    private void playSong(String filePath) {
        Intent intent = new Intent(context, MusicService.class);
        Toast.makeText(context,filePath,Toast.LENGTH_SHORT).show();
        intent.putExtra("file_path",filePath);
        context.startService(intent);
    }

    private void stopSong() {
        MusicService service = new MusicService();
        service.stopMusic();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songName, singerName, songDuration;
        Button btnPause,btnPlay;
//        LinearLayout btnPausePlay;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnPause = (Button) itemView.findViewById(R.id.btn_pause);
            btnPlay = (Button)itemView.findViewById(R.id.btn_play);
            songName = (TextView) itemView.findViewById(R.id.tv_song_name);
            singerName = (TextView) itemView.findViewById(R.id.tv_singer_name);
//            btnPausePlay = (LinearLayout) itemView.findViewById(R.id.btn_pause_play);
            songDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }

    }


}
