package com.abhiandroid.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.abhiandroid.Activities.Adapters.AllSongsAdapter;
import com.abhiandroid.Activities.Fragments.AllSongs;

public class MainActivity extends AppCompatActivity {

    AllSongsAdapter adapter;
    ViewPager simpleViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
        // addOnPageChangeListener event change the tab on slide
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
