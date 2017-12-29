package com.abhiandroid.Activities.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhiandroid.Activities.AudioModel;
import com.abhiandroid.Activities.R;

import java.util.ArrayList;
import java.util.List;

import com.abhiandroid.Activities.Adapters.AllSongsAdapter;

public class AllSongs extends Fragment {

    AllSongsAdapter adapter;

    public AllSongs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.screen_all_songs, container, false);
        List<AudioModel> audioFilesList = getAllAudioFromDevice(getActivity());
        Log.d("", "onCreateView: "+audioFilesList.size());
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_all_songs);
        adapter = new AllSongsAdapter(audioFilesList,getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.myOnDestroy();
    }


    public List<AudioModel> getAllAudioFromDevice(final Context context) {

        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,MediaStore.Audio.AudioColumns.DURATION};
//        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%yourFolderName%"}, null);
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
                String name = path.substring(path.lastIndexOf("/") + 1);

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