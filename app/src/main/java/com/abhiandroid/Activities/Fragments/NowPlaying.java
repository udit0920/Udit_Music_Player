package com.abhiandroid.Activities.Fragments;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.R;
import com.abhiandroid.Activities.Services.MusicService;

import java.io.FileDescriptor;

public class NowPlaying extends Fragment {
    String filePath;
    int position = -1;
    Button previousSong;
    Button nextSong;
    Button pause;
    Button play;
    String singer;
    String songName;
    ImageView songIcon;
    View view;
    int albumID;
    MusicService service;
    BroadcastReceiver receiver;
    BroadcastReceiver autoPlayReceiver;
    private static final String TAG = "NowPlaying";
    static TextView tvSongName, tvSinger;

    public NowPlaying() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("udit.nowPlaying"));
        getActivity().registerReceiver(autoPlayReceiver, new IntentFilter("udit.ButtonState"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);
        tvSongName = (TextView) view.findViewById(R.id.tv_song_name);
        tvSinger = (TextView) view.findViewById(R.id.tv_singer);
        songIcon = (ImageView) view.findViewById(R.id.iv_song_icon);
        previousSong = (Button) view.findViewById(R.id.btn_previous);
        nextSong = (Button) view.findViewById(R.id.btn_next);
        pause = (Button) view.findViewById(R.id.btn_pause);
        play = (Button) view.findViewById(R.id.btn_play);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = new MusicService();
                service.pauseSong();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = new MusicService();
                service.playSong();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }
        });


        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicService.class);
                if (position == -1) {
                    position = 1;
                }
                position = position - 1;
                String path = AllSongs.audioFilesList.get(position).getaPath();
                String songName = AllSongs.audioFilesList.get(position).getaName();
                int albumID = AllSongs.audioFilesList.get(position).getAlbumID();

                intent.putExtra("file_path", path);
                intent.putExtra("position", position);
                getContext().startService(intent);

                tvSongName.setText(songName);
                if (albumID != 0) {
                    Bitmap bmp = getAlbumart(albumID);
                    songIcon.setImageBitmap(bmp);
                }
            }
        });

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicService.class);
                if (position == -1) {
                    position = 0;
                }
                position = position + 1;

                String path = AllSongs.audioFilesList.get(position).getaPath();
                String songName = AllSongs.audioFilesList.get(position).getaName();
                int albumID = AllSongs.audioFilesList.get(position).getAlbumID();

                intent.putExtra("file_path", path);
                intent.putExtra("position", position);
                getContext().startService(intent);

                tvSongName.setText(songName);
                if (albumID != 0) {
                    Bitmap bmp = getAlbumart(albumID);
                    songIcon.setImageBitmap(bmp);
                }
            }
        });
        setupBroadCast();
        return view;
    }

    private void setupBroadCast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                position = intent.getIntExtra("position", 0);
                //        filePath = intent.getStringExtra("filePath");
                songName = AllSongs.audioFilesList.get(position).getaName();
                tvSongName.setText(songName);

                albumID = AllSongs.audioFilesList.get(position).getAlbumID();
                if (albumID != 0) {
                    Bitmap bmp = getAlbumart(albumID);
                    songIcon.setImageBitmap(bmp);
                }

                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }

        };

        autoPlayReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Champ Received ");
                displayInfo(intent);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }
        };


    }

    private void displayInfo(Intent intent) {
        position = intent.getIntExtra("position", 0);
//        filePath = intent.getStringExtra("filePath");
        position = position + 1;
        songName = AllSongs.audioFilesList.get(position).getaName();
        tvSongName.setText(songName);
        albumID = AllSongs.audioFilesList.get(position).getAlbumID();
        if (albumID != 0) {
            Bitmap bmp = getAlbumart(albumID);
            songIcon.setImageBitmap(bmp);
        }

    }

    public Bitmap getAlbumart(int album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = getActivity().getContentResolver().openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
        }
        return bm;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}