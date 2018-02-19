package udit.setia.music.player;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.abhiandroid.Activities.R;

import java.util.ArrayList;
import java.util.List;

import udit.setia.music.player.Adapters.AllSongsAdapter;

/**
 * Created by uditsetia on 27/01/18.
 */

public class SelectSongs extends AppCompatActivity {
	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.select_songs);

		Intent intent = getIntent();
		final String playListName = intent.getStringExtra("playListName");

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);
		myToolbar.setTitleTextColor(getResources().getColor(R.color.actionBar));

		List<AudioModel> audioFilesList = getAllAudioFromDevice(this);
		final AllSongsAdapter adapter = new AllSongsAdapter(getParent(), audioFilesList, this, 2);

		RecyclerView rv = (RecyclerView) findViewById(R.id.rv_select_songs);
		rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		rv.setAdapter(adapter);

		FloatingActionButton btnOk = (FloatingActionButton) findViewById(R.id.fab);
		FloatingActionButton btnCancel = (FloatingActionButton) findViewById(R.id.fab2);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				adapter.saveData(playListName);
				finish();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				finish();
			}
		});


	}


	public List<AudioModel> getAllAudioFromDevice (final Context context) {

		final List<AudioModel> tempAudioList = new ArrayList<>();


		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.Albums.ALBUM_ID};

		Cursor c = context.getContentResolver().query(uri,
				projection,
				null,
				null,
				null);

		if (c != null) {
			while (c.moveToNext()) {

				AudioModel audioModel = new AudioModel();
				String path = c.getString(0);
				String album = c.getString(1);
				String artist = c.getString(2);
				String duration = c.getString(3);
				int albumID = c.getInt(4);

				String name = path.substring(path.lastIndexOf("/") + 1);

				audioModel.setAlbumID(albumID);
				audioModel.setaName(name);
				audioModel.setaDuration(duration);
				audioModel.setaAlbum(album);
				audioModel.setaArtist(artist);
				audioModel.setaPath(path);

				Log.e("Name :" + name, " Album :" + album);
				Log.e("Path :" + path, " Artist :" + artist);

				tempAudioList.add(audioModel);
			}
			c.close();
		}

		return tempAudioList;
	}
}
