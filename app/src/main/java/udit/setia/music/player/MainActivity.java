package udit.setia.music.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.abhiandroid.Activities.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import udit.setia.music.player.Adapters.AllSongsAdapter;
import udit.setia.music.player.Fragments.NowPlaying;
import udit.setia.music.player.Services.MusicService;

public class MainActivity extends AppCompatActivity implements NowPlaying.OnCallbackReceived {

	AllSongsAdapter adapter;
	ViewPager simpleViewPager;
	TabLayout tabLayout;
	BroadcastReceiver notificationUpdateReceiver;

	@Override
	protected void onPause () {
		super.onPause();

	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);
		myToolbar.setTitleTextColor(getResources().getColor(R.color.actionBar));

		// get the reference of ViewPager and TabLayout
		simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
		tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
		// Create a new Tab named "First"
		TabLayout.Tab firstTab = tabLayout.newTab();
		firstTab.setText("Now Playing"); // set the Text for the first Tab
		// first tab
		tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
		// Create a new Tab named "Second"
		TabLayout.Tab secondTab = tabLayout.newTab();
		secondTab.setText("All Songs"); // set the Text for the second Tab
		tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
		// Create a new Tab named "Third"
		TabLayout.Tab thirdTab = tabLayout.newTab();
		thirdTab.setText("PlayList"); // set the Text for the first Tab
		tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

		TabLayout.Tab fourthTab = tabLayout.newTab();
		fourthTab.setText("Current Playlist");
		tabLayout.addTab(fourthTab);

		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected (TabLayout.Tab tab) {
				simpleViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected (TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected (TabLayout.Tab tab) {

			}
		});
		PagerAdapter adapter = new PagerAdapter
				(getSupportFragmentManager(), tabLayout.getTabCount(), tabLayout, simpleViewPager);
		simpleViewPager.setAdapter(adapter);
		simpleViewPager.setOffscreenPageLimit(4);
		// addOnPageChangeListener event change the tab on slide
		simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


		notificationUpdateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {

				String songSource = intent.getStringExtra("songSource");
				int position = intent.getIntExtra("songPosition", 0);
				int pausePlay = intent.getIntExtra("songStatus", 0);
				ArrayList<AudioModel> list = (ArrayList<AudioModel>) intent.getSerializableExtra("songsList");

				//				if (AllSongsAdapter.intent == null) {
				//					AllSongsAdapter.intent = new Intent(MainActivity.this, MusicService.class);
				//				}
				intent = new Intent(MainActivity.this, MusicService.class);
				intent.putExtra("file_path", list.get(position).getaPath());
				intent.putExtra("SONGS_LIST", (Serializable) list);
				intent.putExtra("position", position);
				intent.putExtra("Song_Source", songSource);
				MainActivity.this.startService(intent);

				updateNowPlaying(songSource, list, position, pausePlay);
				UpdatePausePlayState(songSource, position, pausePlay);

			}
		};

		this.registerReceiver(notificationUpdateReceiver, new IntentFilter("udit.update.through.notification"));


	}


	public void updateNowPlaying (String songSource, List<AudioModel> songsList, int position, int pausePlay) {

		String artist = songsList.get(position).getaArtist();
		String songName = songsList.get(position).getaName();
		String songPath = songsList.get(position).getaPath();
		int albumID = songsList.get(position).getAlbumID();

		PagerAdapter.tab1.updateNowPlaying(songsList, songSource, songName, artist, songPath, albumID, position, pausePlay);
	}


	@Override
	protected void onDestroy () {
		super.onDestroy();

	}


	@Override
	public void UpdatePausePlayState (String songSource, int position, int state) {

		if (songSource.equals("allSongs")) {
			PagerAdapter.tab2.updatePausePlayState(position, state);
		} else {
			PagerAdapter.tab4.updatePausePlayState(position, state);
		}

	}

	@Override
	public void UpdateStateNextPrevBtnPressed (int songsListSize, int nxtPrev, int position, String songSource) {

		if (songSource.equals("allSongs")) {
			PagerAdapter.tab2.updatePlayState(songsListSize, nxtPrev, position);
		} else {
			PagerAdapter.tab4.updatePlayState(songsListSize, nxtPrev, position);
		}


	}
}
