package com.abhiandroid.Activities.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abhiandroid.Activities.AudioModel;
import com.abhiandroid.Activities.Fragments.NowPlaying;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by uditsetia on 27/12/17.
 */
public class MusicService extends Service {
	static String PATH_TO_FILE;
	private static final String TAG = "MusicService";
	public static MediaPlayer mediaPlayer;
	static MusicService musicService;
	String songSource = "";
	public static int songDuration = 0;
	static int position;
	ArrayList<AudioModel> songsList;

	public MusicService () {

	}


	public static MediaPlayer getMediaPlayer () {
		return mediaPlayer;
	}

	@Override
	public void onCreate () {
		super.onCreate();
		Log.d(TAG, "Music Service Created");
		mediaPlayer = new MediaPlayer();
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {


		mediaPlayer.stop();

		mediaPlayer.reset();

		songsList = new ArrayList<>();
		songsList = (ArrayList<AudioModel>) intent.getSerializableExtra("SONGS_LIST");
		songSource = intent.getStringExtra("Song_Source");

		position = intent.getIntExtra("position", 0);
		PATH_TO_FILE = intent.getStringExtra("file_path");

		try {
			mediaPlayer.setDataSource(PATH_TO_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared (MediaPlayer mp) {
				mediaPlayer.start();
				songDuration = mp.getDuration();
				NowPlaying.bar.setProgress(0);
				NowPlaying.bar.setMax(mp.getDuration());
			}
		});

		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion (MediaPlayer mp) {
				performOnEnd();
			}

		});

		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError (MediaPlayer mp, int what, int extra) {
				return true;
			}
		});

		return START_NOT_STICKY;

	}


	private void performOnEnd () {

		if (songSource.equals("playList")) {
			Log.d(TAG, "performOnEnd: YO");
			Intent intent = new Intent("udit.ButtonState.PlayLists");
			intent.putExtra("SONGS_LIST", songsList);
			intent.putExtra("btnState", 0);
			intent.putExtra("position", position);
			sendBroadcast(intent);
		} else if (songSource.equals("allSongs")) {
			Intent intent = new Intent("udit.ButtonState");
			intent.putExtra("SONGS_LIST", songsList);
			intent.putExtra("btnState", 0);
			intent.putExtra("position", position);
			sendBroadcast(intent);
		}

	}

	@Override
	public void onDestroy () {
		super.onDestroy();
	}

	@Nullable
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}

	public void pauseSong () {
		mediaPlayer.pause();
	}

	public void playSong () {


		if (mediaPlayer.getCurrentPosition() > 0) {
			Log.d(TAG, "playSong:33 " + mediaPlayer.getCurrentPosition());
			mediaPlayer.start();
		} else {
			Log.d(TAG, "playSong:22 ");
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(PATH_TO_FILE);
				mediaPlayer.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}


}
