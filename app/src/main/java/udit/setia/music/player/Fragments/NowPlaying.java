package udit.setia.music.player.Fragments;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.abhiandroid.Activities.R;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

import udit.setia.music.player.AudioModel;
import udit.setia.music.player.Services.MusicService;

public class NowPlaying extends Fragment implements SeekBar.OnSeekBarChangeListener, Runnable {
	static int position = -1;
	Button previousSong;
	Button nextSong;
	Button pause;
	Button play;
	String songSource;
	static ArrayList<AudioModel> songsList;
	String singer;
	public static SeekBar bar;
	String songName;
	ImageView songIcon;
	View view;
	int albumID;
	int z = 1;
	MusicService service;

	// updateNowPlaying Receiver
	BroadcastReceiver receiver;
	BroadcastReceiver autoPlayReceiver;
	private static final String TAG = "NowPlaying";
	static TextView tvSongName, tvSinger;
	static Thread thread;
	static MediaPlayer mediaPlayer;
	Context context;
	OnCallbackReceived mCallback;
	Handler seekHandler = new Handler();

	public NowPlaying () {


	}

	public interface OnCallbackReceived {
		// It's called when you click pause/play in nowPlaying
		// To update PausePlay in AllSongs/PlayLists
		public void UpdatePausePlayState (String songSource, int position, int state);

		public void UpdateStateNextPrevBtnPressed (int songsListSize, int nxtPrev, int position, String songSource);
	}

	@Override
	public void onAttach (Context context) {
		super.onAttach(context);
		try {
			mCallback = (OnCallbackReceived) context;
		} catch (ClassCastException e) {

		}
	}

	public void updateNowPlaying (List<AudioModel> songsList2, String songSource, String songName, String artist, String path, int albumID, int position2, int pausePlay) {

		songsList = (ArrayList<AudioModel>) songsList2;
		this.songSource = songSource;

		if (thread == null) {
			thread = new Thread(NowPlaying.this);
			thread.start();
		}

		position = position2;
		Bitmap bmp = null;

		if (albumID != 0) {
			bmp = getAlbumart(albumID);
			songIcon.setImageDrawable(null);
			songIcon.setImageBitmap(bmp);
		}

		if (bmp == null) {
			Drawable drawable = getResources().getDrawable(R.drawable.music);
			songIcon.setImageDrawable(null);
			songIcon.setImageDrawable(drawable);
		}

		tvSongName.setText(songName);

		if (pausePlay == 1) {
			play.setVisibility(View.GONE);
			pause.setVisibility(View.VISIBLE);
		} else {
			pause.setVisibility(View.GONE);
			play.setVisibility(View.VISIBLE);
		}

	}


	public NowPlaying (Context context, int z) {
		// Required empty public constructor

		this.context = context;
		this.z = z;
	}


	public void registerReceiver () {

		setupBroadCast();
		getContext().registerReceiver(receiver, new IntentFilter("udit.update.nowPlaying"));
		getContext().registerReceiver(autoPlayReceiver, new IntentFilter("udit.ButtonState"));

	}

	public void unRegisterReceiver () {

		getActivity().unregisterReceiver(receiver);
		getActivity().unregisterReceiver(autoPlayReceiver);

	}


	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume () {
		super.onResume();
		registerReceiver();
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		view = inflater.inflate(R.layout.fragment_second, container, false);
		bar = (SeekBar) view.findViewById(R.id.seekBar1);
		tvSongName = (TextView) view.findViewById(R.id.tv_song_name);
		tvSinger = (TextView) view.findViewById(R.id.tv_singer);
		songIcon = (ImageView) view.findViewById(R.id.iv_song_icon);
		previousSong = (Button) view.findViewById(R.id.btn_previous);
		nextSong = (Button) view.findViewById(R.id.btn_next);
		pause = (Button) view.findViewById(R.id.btn_pause);
		play = (Button) view.findViewById(R.id.btn_play);

		bar.setEnabled(true);
		bar.setOnSeekBarChangeListener(this);

		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				service = new MusicService();
				service.pauseSong();
				pause.setVisibility(View.GONE);
				play.setVisibility(View.VISIBLE);
				mCallback.UpdatePausePlayState(songSource, position, 0);
			}
		});

		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {

				if (position == -1) {
					playChamp();
				} else {
					service = new MusicService();
					service.playSong();
					play.setVisibility(View.GONE);
					pause.setVisibility(View.VISIBLE);
					mCallback.UpdatePausePlayState(songSource, position, 1);
				}
			}
		});


		previousSong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {

				position = position - 1;

				Intent intent = new Intent(getContext(), MusicService.class);

				if (position == -2) {
					playDirectly();
					position = 0;
				}

				if (position == -1) {
					position = 0;
				}
				Bitmap bmp = null;

				String path = songsList.get(position).getaPath();
				String songName = songsList.get(position).getaName();
				int albumID = songsList.get(position).getAlbumID();

				if (songSource != null) {
					intent.putExtra("Song_Source", songSource);
				}

				intent.putExtra("file_path", path);
				intent.putExtra("position", position);
				intent.putExtra("SONGS_LIST", songsList);

				getContext().startService(intent);

				if (thread == null) {
					thread = new Thread(NowPlaying.this);
					thread.start();
				}

				tvSongName.setText(songName);
				if (albumID != 0) {
					bmp = getAlbumart(albumID);
					songIcon.setImageDrawable(null);
					songIcon.setImageBitmap(bmp);
				}

				if (bmp == null) {
					Drawable drawable = getResources().getDrawable(R.drawable.music);
					songIcon.setImageDrawable(null);
					songIcon.setImageDrawable(drawable);
				}

				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);

				//				if (songSource.equals("playList")) {
				//					Intent broadcastIntent = new Intent("udit.updateCurrentPlayListButtonStateFromNowPlaying");
				//					broadcastIntent.putExtra("position", position);
				//					getContext().sendBroadcast(broadcastIntent);
				//				} else {
				//					Intent broadcastIntent = new Intent("udit.updateButtonStateFromNowPlaying");
				//					broadcastIntent.putExtra("position", position);
				//					getContext().sendBroadcast(broadcastIntent);
				//				}

				mCallback.UpdateStateNextPrevBtnPressed(songsList.size(), 0, position, songSource);


			}
		});

		nextSong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {

				Intent intent = new Intent(getContext(), MusicService.class);

				position = position + 1;

				// if song not selected form all songs or any playlist
				if (position == 0) {
					playDirectly();
				}

				if (position >= songsList.size()) {
					position = 0;
				}

				Bitmap bmp = null;

				String path = songsList.get(position).getaPath();
				String songName = songsList.get(position).getaName();
				int albumID = songsList.get(position).getAlbumID();

				Log.d(TAG, "Song Source  " + songSource);

				if (songSource != null) {
					intent.putExtra("Song_Source", songSource);
				}

				intent.putExtra("file_path", path);
				intent.putExtra("position", position);


				Log.d(TAG, "Song Source " + songsList.size());
				intent.putExtra("SONGS_LIST", songsList);
				getContext().startService(intent);

				if (thread == null) {
					thread = new Thread(NowPlaying.this);
					thread.start();
				}

				tvSongName.setText(songName);

				if (albumID != 0) {
					bmp = getAlbumart(albumID);
					songIcon.setImageBitmap(bmp);
				}

				if (bmp == null) {
					Drawable drawable = getResources().getDrawable(R.drawable.music);
					songIcon.setImageDrawable(null);
					songIcon.setImageDrawable(drawable);
				}

				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);

				mCallback.UpdateStateNextPrevBtnPressed(songsList.size(), 1, position, songSource);
				//				if (songSource.equals("playList")) {
				//					Intent broadcastIntent = new Intent("udit.updateCurrentPlayListButtonStateFromNowPlaying");
				//					broadcastIntent.putExtra("position", position);
				//					getContext().sendBroadcast(broadcastIntent);
				//				} else {
				//					Intent broadcastIntent = new Intent("udit.updateButtonStateFromNowPlaying");
				//					broadcastIntent.putExtra("position", position);
				//					getContext().sendBroadcast(broadcastIntent);
				//				}


			}
		});
		setupBroadCast();
		return view;
	}

	private void playChamp () {
		position = position - 1;

		Intent intent = new Intent(getContext(), MusicService.class);

		if (position == -2) {
			playDirectly();
			position = 0;
		}

		if (position == -1) {
			position = 0;
		}
		Bitmap bmp = null;

		String path = songsList.get(position).getaPath();
		String songName = songsList.get(position).getaName();
		int albumID = songsList.get(position).getAlbumID();

		if (songSource != null) {
			intent.putExtra("Song_Source", songSource);
		}

		intent.putExtra("file_path", path);
		intent.putExtra("position", position);
		intent.putExtra("SONGS_LIST", songsList);

		getContext().startService(intent);

		if (thread == null) {
			thread = new Thread(NowPlaying.this);
			thread.start();
		}

		tvSongName.setText(songName);
		if (albumID != 0) {
			bmp = getAlbumart(albumID);
			songIcon.setImageDrawable(null);
			songIcon.setImageBitmap(bmp);
		}

		if (bmp == null) {
			Drawable drawable = getResources().getDrawable(R.drawable.music);
			songIcon.setImageDrawable(null);
			songIcon.setImageDrawable(drawable);
		}

		play.setVisibility(View.GONE);
		pause.setVisibility(View.VISIBLE);

		//				if (songSource.equals("playList")) {
		//					Intent broadcastIntent = new Intent("udit.updateCurrentPlayListButtonStateFromNowPlaying");
		//					broadcastIntent.putExtra("position", position);
		//					getContext().sendBroadcast(broadcastIntent);
		//				} else {
		//					Intent broadcastIntent = new Intent("udit.updateButtonStateFromNowPlaying");
		//					broadcastIntent.putExtra("position", position);
		//					getContext().sendBroadcast(broadcastIntent);
		//				}

		mCallback.UpdateStateNextPrevBtnPressed(songsList.size(), 0, position, songSource);


	}

	private void playDirectly () {
		songsList = (ArrayList<AudioModel>) AllSongs.audioFilesList;
		bar = (SeekBar) view.findViewById(R.id.seekBar1);
		bar.setEnabled(true);
		bar.setOnSeekBarChangeListener(NowPlaying.this);
		songSource = "allSongs";
	}


	public void seekUpdation () {
		Log.d(TAG, "run: 32");

		while (true) {
			int currentPosition = 0;
			Log.d(TAG, "run: 42");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (MusicService.songDuration != 0) {

				while (currentPosition < MusicService.songDuration) {
					try {
						//workerThread
						Log.d(TAG, "run: 52");

						Log.d(TAG, "seekUpdation: " + Thread.activeCount());
						Thread.sleep(1000);
						if (MusicService.mediaPlayer.isPlaying()) {
							currentPosition = MusicService.mediaPlayer.getCurrentPosition();
						} else if (currentPosition > 0) {

						} else if (currentPosition == 0) {
							bar.setMax(100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					bar.setProgress(currentPosition);
				}
			}
		}

	}


	private void setupBroadCast () {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {

				mediaPlayer = MusicService.getMediaPlayer();
				if (songsList == null)
					songsList = new ArrayList<>();

				if (thread == null) {
					thread = new Thread(NowPlaying.this);
					thread.start();
				}

				Bitmap bmp = null;

				int songPosition = intent.getIntExtra("position", 0);

				ArrayList<AudioModel> audioList = new ArrayList<>();

				audioList = (ArrayList<AudioModel>) intent.getSerializableExtra("Songs_List");
				Log.d(TAG, "onReceive:33 " + songsList);
				Log.d(TAG, "onReceive:44 " + audioList);

				if (position == songPosition && audioList.size() == songsList.size()) {
					Log.d(TAG, "onReceive:11 ");
					play.setVisibility(View.VISIBLE);
					pause.setVisibility(View.GONE);
				} else {
					Log.d(TAG, "onReceive:22 ");
					play.setVisibility(View.GONE);
					pause.setVisibility(View.VISIBLE);
				}
				position = songPosition;
				songsList = audioList;
				songSource = intent.getStringExtra("Song_Source");
				songName = songsList.get(position).getaName();
				tvSongName.setText(songName);

				albumID = songsList.get(position).getAlbumID();
				Log.d(TAG, "onReceive: champ " + songsList.size());
				if (albumID != 0) {

					bmp = getAlbumart(albumID);
					songIcon.setImageDrawable(null);
					songIcon.setImageBitmap(bmp);
				}

				if (bmp == null) {
					Drawable drawable = getResources().getDrawable(R.drawable.music);
					songIcon.setImageDrawable(null);
					songIcon.setImageDrawable(drawable);
				}

			}

		};

		autoPlayReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {
				Log.d(TAG, "Champ Received ");

				if (thread == null) {
					thread = new Thread(NowPlaying.this);
					thread.start();
				}
				displayInfo(intent);
				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);
			}
		};


	}

	private void displayInfo (Intent intent) {

		Bitmap bmp = null;
		position = intent.getIntExtra("position", 0);

		ArrayList<AudioModel> list = new ArrayList<>();
		list = (ArrayList<AudioModel>) intent.getSerializableExtra("SONGS_LIST");

		position = position + 1;

		if (position == list.size()) {
			position = 0;
		}

		songName = list.get(position).getaName();
		tvSongName.setText(songName);
		albumID = list.get(position).getAlbumID();

		if (albumID != 0) {
			bmp = getAlbumart(albumID);
			songIcon.setImageDrawable(null);
			songIcon.setImageBitmap(bmp);
		}

		if (bmp == null) {
			Drawable drawable = getResources().getDrawable(R.drawable.music);
			songIcon.setImageDrawable(null);
			songIcon.setImageDrawable(drawable);
		}

	}

	public Bitmap getAlbumart (int album_id) {
		Bitmap bm = null;
		try {
			final Uri sArtworkUri = Uri
					.parse("content://media/external/audio/albumart");

			Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

			ParcelFileDescriptor pfd = getActivity().getContentResolver().openFileDescriptor(uri, "r");

			if (pfd != null) {
				FileDescriptor fd = pfd.getFileDescriptor();
				bm = BitmapFactory.decodeFileDescriptor(fd);
			}
		} catch (Exception e) {
		}
		return bm;
	}

	@Override
	public void onPause () {
		super.onPause();
		Log.d(TAG, "updateNowPlaying exit ");
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		unRegisterReceiver();
	}

	@Override
	public void onDestroyView () {
		super.onDestroyView();
	}


	@Override
	public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
		try {
			if (MusicService.mediaPlayer.isPlaying() || MusicService.mediaPlayer != null) {
				if (fromUser) {
					seekBar.setMax(MusicService.mediaPlayer.getDuration());
					MusicService.mediaPlayer.seekTo(progress);
				}
			} else if (MusicService.mediaPlayer == null) {
				//Toast.makeText(getContext(), "Media is not running",
				//						Toast.LENGTH_SHORT).show();
				seekBar.setProgress(0);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onStartTrackingTouch (SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch (SeekBar seekBar) {

	}

	@Override
	public void run () {

		int x = 0;
		while (x == 0) {

			Log.d(TAG, "run: 222 " + bar + " " + MusicService.mediaPlayer);
			MediaPlayer mp = MusicService.getMediaPlayer();
			if (bar != null && mp != null) {
				Log.d(TAG, "run: 22");
				seekUpdation();
				x = 1;

			}
		}
	}

}