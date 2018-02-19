package udit.setia.music.player.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhiandroid.Activities.R;

import java.util.ArrayList;

import udit.setia.music.player.Adapters.PlayListSongsAdapter;
import udit.setia.music.player.AudioModel;
import udit.setia.music.player.DbHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by uditsetia on 05/02/18.
 */

public class CurrentPlayList extends Fragment {

	//	TabLayout tabLayout;
	static PlayListSongsAdapter adapter;
	BroadcastReceiver receiver;
	ArrayList<AudioModel> songsList;

	@Override
	public void onResume () {
		super.onResume();
		getContext().registerReceiver(receiver, new IntentFilter("udit.selected.playList"));

		// May be
		if (adapter != null) {
			Log.d(TAG, "onResume:current ");
		}
	}

	public void updatePausePlayState (int position, int state) {

		adapter.updatePausePlayState(position, state);
	}

	public void updatePlayState (int songsListSize, int nxtPrev, int position) {

		adapter.updatePlayState(songsListSize, nxtPrev, position);
	}

	@Override
	public void onPause () {
		super.onPause();
		getContext().unregisterReceiver(receiver);
		if (adapter != null) {
			adapter.myOnDestroy();
		}

	}

	public CurrentPlayList () {
	}


	@Nullable
	@Override
	public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();
		View view = inflater.inflate(R.layout.screen_all_songs, container, false);
		final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_all_songs);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {
				songsList = new ArrayList<>();

				String playListName = intent.getStringExtra("selectedPlaylist");

				DbHelper dbHelper = DbHelper.getInstance(getContext());

				songsList = dbHelper.getPlayListSongs(playListName);
				//Toast.makeText(context, "Received " + songsList.size(), Toast.LENGTH_LONG).show();
				adapter = new PlayListSongsAdapter(getActivity(), songsList, context, 1);
				adapter.registerReceiver();

				rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
				rv.setAdapter(adapter);
			}
		};
		return view;
	}


}
