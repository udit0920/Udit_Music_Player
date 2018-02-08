package com.abhiandroid.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.abhiandroid.Activities.Adapters.PlayListSongsAdapter;
import com.abhiandroid.Activities.Fragments.NowPlaying;

/**
 * Created by uditsetia on 29/01/18.
 */

public class PlayListActivity extends AppCompatActivity {

	PlayListSongsAdapter adapter;
	NowPlaying np;

	@Override
	public void onBackPressed () {
		this.finish();
	}

	@Override
	public boolean onSupportNavigateUp () {
		super.onSupportNavigateUp();
		//		finish();
		return true;
	}

	@Override
	protected void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selected_playlist_activity);
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);
		myToolbar.setTitleTextColor(getResources().getColor(R.color.actionBar));

		Drawable upArrow;
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
			upArrow.setColorFilter(getResources().getColor(R.color.actionBar), PorterDuff.Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String selectedPlaylist = getIntent().getStringExtra("selectedPlayList");
		this.setTitle(selectedPlaylist);

		DbHelper db = DbHelper.getInstance(this);
		adapter = new PlayListSongsAdapter(db.getPlayListSongs(selectedPlaylist), this, 1);
		RecyclerView rv = (RecyclerView) findViewById(R.id.rv_selected_playlist);
		rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		rv.setAdapter(adapter);
	}


	@Override
	public void onResume () {
		super.onResume();
		adapter.registerReceiver();
		np = new NowPlaying(this,2);
		np.registerReceiver();

	}

	@Override
	public void onPause () {
		super.onPause();
		adapter.myOnDestroy();
	}


}
