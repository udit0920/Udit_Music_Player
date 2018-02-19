package udit.setia.music.player.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhiandroid.Activities.R;

import java.util.ArrayList;

import udit.setia.music.player.Adapters.PlayListAdapter;
import udit.setia.music.player.CreatePlayListDialog;
import udit.setia.music.player.DbHelper;

public class PlayLists extends Fragment {

	TabLayout tabLayout;
	ViewPager viewPager;
	DbHelper db;
	BroadcastReceiver receiver;
	ArrayList<String> allPlayLists;

	public PlayLists (TabLayout tabLayout, ViewPager viewPager) {
		// Required empty public constructor
		this.tabLayout = tabLayout;
		this.viewPager = viewPager;
	}

	@Override
	public void onResume () {
		super.onResume();
	}

	@Override
	public void onPause () {
		super.onPause();
		//		//Toast.makeText(getContext(), "Receiver unregistered", Toast.LENGTH_SHORT).show();
		//		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflateView = inflater.inflate(R.layout.fragment_third, container, false);

		FloatingActionButton btnCreatePlayList = (FloatingActionButton) inflateView.findViewById(R.id.fab);
		RecyclerView rv = (RecyclerView) inflateView.findViewById(R.id.rv_playList_select_songs);

		db = DbHelper.getInstance(getContext());
		allPlayLists = db.getAllPlayLists();

		//Toast.makeText(getContext(), "reciever st " + allPlayLists.size(), Toast.LENGTH_SHORT).show();

		final PlayListAdapter adapter = new PlayListAdapter(getActivity(), allPlayLists, tabLayout, viewPager);
		rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		rv.setAdapter(adapter);

		btnCreatePlayList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent intent = new Intent(getActivity(), CreatePlayListDialog.class);
				startActivity(intent);
			}
		});

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive (Context context, Intent intent) {

				ArrayList<String> dummyObj = db.getAllPlayLists();
				String addedPlayList = dummyObj.get(dummyObj.size() - 1);

				allPlayLists.add(addedPlayList);

				adapter.notifyDataSetChanged();
				//Toast.makeText(getContext(), "reciever Received " + allPlayLists.size(), Toast.LENGTH_SHORT).show();

			}
		};
		//Toast.makeText(getContext(), "Receiver registered", Toast.LENGTH_SHORT).show();
		getActivity().registerReceiver(receiver, new IntentFilter("udit.update.playLists"));

		return inflateView;
	}

}