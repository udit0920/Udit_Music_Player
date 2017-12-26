package com.abhiandroid.tablayoutexample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Fragments.NowPlaying;
import Fragments.AllSongs;
import Fragments.PlayLists;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
 
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                NowPlaying tab1 = new NowPlaying();
                return tab1;
            case 1:
                AllSongs tab2 = new AllSongs();
                return tab2;
            case 2:
                PlayLists tab3 = new PlayLists();
                return tab3;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}