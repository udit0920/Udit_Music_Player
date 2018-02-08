package com.abhiandroid.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.abhiandroid.Activities.Fragments.AllSongs;
import com.abhiandroid.Activities.Fragments.CurrentPlayList;
import com.abhiandroid.Activities.Fragments.NowPlaying;
import com.abhiandroid.Activities.Fragments.PlayLists;

public class PagerAdapter extends FragmentStatePagerAdapter {
	int mNumOfTabs;
	TabLayout tabLayout;
	ViewPager viewPager;

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
				NowPlaying tab1 = new NowPlaying();
				return tab1;

			case 1:
				AllSongs tab2 = new AllSongs();
				return tab2;

			case 2:
				PlayLists tab3 = new PlayLists(tabLayout, viewPager );
				return tab3;

			case 3:
				CurrentPlayList tab4 = new CurrentPlayList();
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