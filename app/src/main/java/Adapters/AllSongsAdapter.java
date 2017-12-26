package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abhiandroid.tablayoutexample.AudioModel;
import com.abhiandroid.tablayoutexample.R;

import java.util.List;

/**
 * Created by uditsetia on 26/12/17.
 */
public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.MyViewHolder> {

    List<AudioModel> songsList;

    public AllSongsAdapter(List<AudioModel> songsList) {

        this.songsList = songsList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_songs_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.songName.setText(songsList.get(position).getaName());
        holder.singerName.setText(songsList.get(position).getaArtist());

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songName, singerName;
        LinearLayout btnPausePlay;

        public MyViewHolder(View itemView) {
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.tv_song_name);
            singerName = (TextView) itemView.findViewById(R.id.tv_singer_name);
            btnPausePlay = (LinearLayout) itemView.findViewById(R.id.btn_pause_play);
        }

    }


}
