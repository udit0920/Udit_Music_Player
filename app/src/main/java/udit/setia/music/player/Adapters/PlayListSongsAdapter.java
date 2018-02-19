package udit.setia.music.player.Adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhiandroid.Activities.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import udit.setia.music.player.AudioModel;
import udit.setia.music.player.DbHelper;
import udit.setia.music.player.Fragments.AllSongs;
import udit.setia.music.player.MainActivity;
import udit.setia.music.player.Services.MusicService;

/**
 * Created by uditsetia on 03/02/18.
 */

public class PlayListSongsAdapter extends RecyclerView.Adapter<PlayListSongsAdapter.MyViewHolder> {

	private static final String TAG = "PlayListSongsAdapter";
	public static BroadcastReceiver btnStateReceiver;
	public static BroadcastReceiver receiver;
	public static BroadcastReceiver receiverUpdatePausePlay;
	List<AudioModel> songsList;
	public Context context;
	int btnState;
	public int x = 0;
	public int y = 0;
	int viewType;
	Intent intent;
	MainActivity activity;

	public PlayListSongsAdapter (Activity activity, List<AudioModel> songsList, Context context, int viewType) {

		this.activity = (MainActivity) activity;
		this.songsList = songsList;

		if (this.context == null) {
			this.context = context;
		}

		this.viewType = viewType;
	}

	public PlayListSongsAdapter () {

	}

	public void updatePlayState (int songsListSize, int nxtPrev, int position) {

		// 1 : Next Button pressed in NowPlaying
		// 0 : Prev Button pressed in NowPlaying

		if (nxtPrev == 1) {
			if (position != 0) {
				songsList.get(position - 1).setButtonState(0);
				songsList.get(position).setButtonState(1);
				this.notifyItemChanged(position - 1);
				this.notifyItemChanged(position);
			} else {
				songsList.get(songsListSize - 1).setButtonState(0);
				songsList.get(position).setButtonState(1);
				this.notifyItemChanged(songsListSize - 1);
				this.notifyItemChanged(position);
			}
		} else {
			songsList.get(position + 1).setButtonState(0);
			songsList.get(position).setButtonState(1);
			this.notifyItemChanged(position + 1);
			this.notifyItemChanged(position);
		}
	}


	public void updatePausePlayState (int position, int btnState) {


		songsList.get(position).setButtonState(btnState);
		this.notifyItemChanged(position);

	}

	@Override
	public PlayListSongsAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.all_songs_layout, parent, false);
		setupBroadCast();
		return new PlayListSongsAdapter.MyViewHolder(view);
	}

	public void setupBroadCast () {

		btnStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {

				for (int i = 0; i < songsList.size(); i++) {
					songsList.get(i).setButtonState(0);
				}
				PlayListSongsAdapter.this.notifyDataSetChanged();
			}
		};


		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {

				btnState = intent.getIntExtra("btnState", 0);
				int position = intent.getIntExtra("position", 0);
				songsList = (ArrayList<AudioModel>) intent.getSerializableExtra("SONGS_LIST");
				Intent intent2 = new Intent(context, MusicService.class);
				if (position + 1 >= songsList.size()) {
					position = -1;
				}
				intent2.putExtra("SONGS_LIST", (Serializable) songsList);
				intent2.putExtra("file_path", songsList.get(position + 1).getaPath());
				intent2.putExtra("position", position + 1);
				intent2.putExtra("Song_Source", "playList");
				context.startService(intent2);

				for (int i = 0; i < songsList.size(); i++) {
					if (i != position + 1) {
						songsList.get(i).setButtonState(0);
					} else {
						songsList.get(i).setButtonState(1);
					}
				}
				PlayListSongsAdapter.this.notifyDataSetChanged();
				//				updateNowPlaying(position + 1);
			}
		};
		if (x == 0) {
			x = 1;
			context.registerReceiver(receiver, new IntentFilter("udit.ButtonState.PlayLists"));
			//			context.registerReceiver(btnStateReceiver, new IntentFilter("udit.song.selected.from.all.songs"));
		}
		//		receiverUpdatePausePlay = new BroadcastReceiver() {
		//			@Override
		//			public void onReceive (Context context, Intent intent) {
		//				int position = intent.getIntExtra("position", 0);
		//				Log.d(TAG, "PAUSEPLAY  " + position);
		//				for (int i = 0; i < songsList.size(); i++) {
		//					if (i != position) {
		//						songsList.get(i).setButtonState(0);
		//					} else {
		//						songsList.get(i).setButtonState(1);
		//					}
		//				}
		//				PlayListSongsAdapter.this.notifyDataSetChanged();
		//			}
		//		};

		//		if (y == 0) {
		//			y = 1;
		//			context.registerReceiver(receiverUpdatePausePlay, new IntentFilter("udit.updateCurrentPlayListButtonStateFromNowPlaying"));
		//		}

	}

	public void registerReceiver () {
		//		setupBroadCast();
	}

	public void myOnDestroy () {
		if (context != null && receiver != null) {
			LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
			LocalBroadcastManager.getInstance(context).unregisterReceiver(receiverUpdatePausePlay);
		}
	}


	@Override
	public void onBindViewHolder (final PlayListSongsAdapter.MyViewHolder holder, final int position) {

		int songDuration = Integer.valueOf(songsList.get(position).getDuration());
		holder.songName.setText(songsList.get(position).getaName());
		holder.singerName.setText(songsList.get(position).getaArtist());
		holder.songDuration.setText(getProperDuration(songDuration));
		final String filePath = songsList.get(position).getaPath();

		final int btnState = songsList.get(position).getButtonState();

		if (btnState == 1) {
			holder.cb.setVisibility(View.GONE);
			holder.btnPlay.setVisibility(View.GONE);
			holder.btnPause.setVisibility(View.VISIBLE);
		} else {
			holder.cb.setVisibility(View.GONE);
			holder.btnPause.setVisibility(View.GONE);
			holder.btnPlay.setVisibility(View.VISIBLE);
		}

		if (viewType == 2) {

			holder.btnPause.setVisibility(View.GONE);
			holder.btnPlay.setVisibility(View.GONE);
			holder.cb.setVisibility(View.VISIBLE);

			if (songsList.get(position).getCheckBoxStatus()) {
				holder.cb.setChecked(true);
			} else {
				holder.cb.setChecked(false);
			}
		}

		holder.musicRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {

				if (viewType == 1) {
					if (btnState == 0) {
						songsList.get(position).setButtonState(1);
						for (int i = 0; i < songsList.size(); i++) {
							if (i != position) {
								songsList.get(i).setButtonState(0);
							}
						}
						playSong(filePath, position);
						PlayListSongsAdapter.this.notifyDataSetChanged();
					} else {
						songsList.get(position).setButtonState(0);
						stopSong(position);
					}
				} else {

					if (!songsList.get(position).getCheckBoxStatus()) {
						songsList.get(position).setCheckBoxStatus(true);
						PlayListSongsAdapter.this.notifyDataSetChanged();
					} else {
						songsList.get(position).setCheckBoxStatus(false);
						PlayListSongsAdapter.this.notifyDataSetChanged();
					}

				}

				Intent intent = new Intent("udit.btnState.from.Current.PlayList");
				context.sendBroadcast(intent);

			}
		});

	}

	//	private void stopSong (int position) {
	//
	//		MusicService.mediaPlayer.stop();
	//		NowPlaying.bar.setProgress(0);
	//		//		MusicService.mediaPlayer.reset();
	//		this.notifyItemChanged(position);
	//
	//	}

	private void stopSong (int position) {

		MusicService.mediaPlayer.pause();
		this.notifyItemChanged(position);
		updateNowPlaying("playList", position, 0);

	}


	private String getProperDuration (int songDuration) {

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
	public int getItemCount () {
		Log.d(TAG, "getItemCount: " + songsList.size());
		return songsList.size();
	}


	public void saveData (String playListName) {

		ArrayList<AudioModel> listData = new ArrayList<>(songsList.size());

		for (int i = 0; i < songsList.size(); i++) {
			if (songsList.get(i).getCheckBoxStatus()) {
				listData.add(songsList.get(i));
			}
		}

		DbHelper dbHelper = DbHelper.getInstance(context);
		dbHelper.savePlayList(playListName, listData);
	}

	private void updateNowPlaying (String songSource, int position, int pausePlay) {


		if (songsList == null) {
			songsList = AllSongs.audioFilesList;
		}

		List<AudioModel> songsList = this.songsList;
		activity.updateNowPlaying(songSource, songsList, position, pausePlay);

	}

	private void playSong (String filePath, int position) {

		intent = new Intent(context, MusicService.class);
		Log.d(TAG, "playSong: " + songsList.size());
		intent.putExtra("SONGS_LIST", (Serializable) songsList);
		intent.putExtra("file_path", filePath);
		intent.putExtra("position", position);
		intent.putExtra("Song_Source", "playList");

		context.startService(intent);
		updateNowPlaying("playList", position, 1);

	}


	//	private void updateNowPlaying (int position) {
	//
	//		Intent intent = new Intent("udit.update.nowPlaying");
	//		intent.putExtra("Song_Source", "playList");
	//		intent.putExtra("position", position);
	//		intent.putExtra("Songs_List", (Serializable) songsList);
	//		intent.putExtra("songName", songsList.get(position).getaName());
	//		intent.putExtra("singer", songsList.get(position).getaArtist());
	//		intent.putExtra("filePath", songsList.get(position).getaPath());
	//		int albumID = songsList.get(position).getAlbumID();
	//		intent.putExtra("albumID", albumID);
	//		context.sendBroadcast(intent);
	//
	//	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		RelativeLayout musicRow;
		CheckBox cb;
		TextView songName, singerName, songDuration;
		ImageView btnPause, btnPlay;

		public MyViewHolder (View itemView) {
			super(itemView);
			musicRow = (RelativeLayout) itemView.findViewById(R.id.music_row);
			btnPause = (ImageView) itemView.findViewById(R.id.btn_pause);
			btnPlay = (ImageView) itemView.findViewById(R.id.btn_play);
			songName = (TextView) itemView.findViewById(R.id.tv_song_name);
			singerName = (TextView) itemView.findViewById(R.id.tv_singer_name);
			songDuration = (TextView) itemView.findViewById(R.id.tv_duration);
			cb = (CheckBox) itemView.findViewById(R.id.cb_playlist);
		}

	}


}

