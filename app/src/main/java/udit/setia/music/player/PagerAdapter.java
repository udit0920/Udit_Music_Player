package udit.setia.music.player;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import udit.setia.music.player.Fragments.AllSongs;
import udit.setia.music.player.Fragments.CurrentPlayList;
import udit.setia.music.player.Fragments.NowPlaying;
import udit.setia.music.player.Fragments.PlayLists;

public class PagerAdapter extends FragmentStatePagerAdapter {
	int mNumOfTabs;
	TabLayout tabLayout;
	ViewPager viewPager;
	static NowPlaying tab1;
	static AllSongs tab2;
	static PlayLists tab3;
	static CurrentPlayList tab4;


	public PagerAdapter (FragmentManager fm, int NumOfTabs, TabLayout tabLayout, ViewPager viewPager) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
		this.tabLayout = tabLayout;
		this.viewPager = viewPager;
	}

	@Override
	public Fragment getItem (int position) {

		switch (position) {
			case 0:
				tab1 = new NowPlaying();
				return tab1;

			case 1:
				tab2 = new AllSongs();
				return tab2;

			case 2:
				tab3 = new PlayLists(tabLayout, viewPager);
				return tab3;

			case 3:
				tab4 = new CurrentPlayList();
				return tab4;

			default:
				return null;
		}
	}

	@Override
	public int getCount () {
		return mNumOfTabs;
	}
}