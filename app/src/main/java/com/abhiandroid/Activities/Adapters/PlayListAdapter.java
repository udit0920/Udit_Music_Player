package com.abhiandroid.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.R;

import java.util.ArrayList;

/**
 * Created by uditsetia on 29/01/18.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.MyViewHolder> {

	ArrayList<String> playLists;
	Context context;
	TabLayout tabLayout;
	ViewPager viewPager;
	ViewGroup parent;

	public PlayListAdapter (Context context, ArrayList<String> playLists, TabLayout tabLayout, ViewPager viewPager) {

		this.playLists = playLists;
		this.context = context;
		this.tabLayout = tabLayout;
		this.viewPager = viewPager;

	}

	@Override
	public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		this.parent = parent;
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.playlist_name_layout, parent, false);
		return new PlayListAdapter.MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder (MyViewHolder holder, final int position) {

		holder.tvPlayListName.setText(playLists.get(position));
		holder.touchableView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent intent = new Intent("udit.selected.playList");
				Toast.makeText(context,"AnswerDDDD "+playLists.get(position),Toast.LENGTH_LONG).show();
				intent.putExtra("selectedPlaylist",playLists.get(position));
				context.sendBroadcast(intent);
				tabLayout.setScrollPosition(3, 0f, true);
				viewPager.setCurrentItem(3);
			}
		});

	}

	@Override
	public int getItemCount () {
		return playLists.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		TextView tvPlayListName;
		View touchableView;

		public MyViewHolder (View itemView) {
			super(itemView);
			tvPlayListName = (TextView) itemView.findViewById(R.id.tv_playList_names);
			touchableView = (View) itemView.findViewById(R.id.view_playList_option);
		}
	}


}
