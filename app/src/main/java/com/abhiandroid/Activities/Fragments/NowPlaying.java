package com.abhiandroid.Activities.Fragments;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhiandroid.Activities.R;
import com.abhiandroid.Activities.Services.MusicService;

import java.io.FileDescriptor;

public class NowPlaying extends Fragment implements SeekBar.OnSeekBarChangeListener, Runnable {
    String filePath;
    int position = -1;
    Button previousSong;
    Button nextSong;
    Button pause;
    Button play;
    String singer;
    SeekBar bar;
    String songName;
    ImageView songIcon;
    View view;
    int albumID;
    MusicService service;
    BroadcastReceiver receiver;
    BroadcastReceiver autoPlayReceiver;
    private static final String TAG = "NowPlaying";
    static TextView tvSongName, tvSinger;
    Thread thread;

    Handler seekHandler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
//            seekUpdation();
        }
    };

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
        bar = (SeekBar) view.findViewById(R.id.seekBar1);
        tvSongName = (TextView) view.findViewById(R.id.tv_song_name);
        tvSinger = (TextView) view.findViewById(R.id.tv_singer);
        songIcon = (ImageView) view.findViewById(R.id.iv_song_icon);
        previousSong = (Button) view.findViewById(R.id.btn_previous);
        nextSong = (Button) view.findViewById(R.id.btn_next);
        pause = (Button) view.findViewById(R.id.btn_pause);
        play = (Button) view.findViewById(R.id.btn_play);

//        thread = new Thread(this);

        bar.setEnabled(true);
        bar.setOnSeekBarChangeListener(this);

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
                Bitmap bmp = null;

                String path = AllSongs.audioFilesList.get(position).getaPath();
                String songName = AllSongs.audioFilesList.get(position).getaName();
                int albumID = AllSongs.audioFilesList.get(position).getAlbumID();

                intent.putExtra("file_path", path);
                intent.putExtra("position", position);
                getContext().startService(intent);

                tvSongName.setText(songName);
                if (albumID != 0) {
                    bmp = getAlbumart(albumID);
                    songIcon.setImageDrawable(null);
                    songIcon.setImageBitmap(bmp);
                }

                if (bmp == null) {
                    Drawable drawable = getResources().getDrawable(R.drawable.music);
                    songIcon.setImageDrawable(null);
                    songIcon.setImageDrawable(drawable);
                }

                bar.setProgress(0);
                bar.setMax(MusicService.mediaPlayer.getDuration());

                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);

                Intent broadcastIntent = new Intent("udit.updateButtonStateFromNowPlaying");
                broadcastIntent.putExtra("position", position);
                getContext().sendBroadcast(broadcastIntent);

//                thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "run: champ");
//                        int currentPosition = MusicService.mediaPlayer.getCurrentPosition();
//                        int total = MusicService.mediaPlayer.getDuration();
//                        while (MusicService.mediaPlayer != null && currentPosition < total) {
//                            try {
//                                currentPosition = MusicService.mediaPlayer.getCurrentPosition();
//                                bar.setProgress(currentPosition);
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                return;
//                            } catch (Exception e) {
//                                return;
//
//                            }
//                        }
//                    }
//                });
//                thread.start();

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

                Bitmap bmp = null;

                String path = AllSongs.audioFilesList.get(position).getaPath();
                String songName = AllSongs.audioFilesList.get(position).getaName();
                int albumID = AllSongs.audioFilesList.get(position).getAlbumID();

                intent.putExtra("file_path", path);
                intent.putExtra("position", position);
                getContext().startService(intent);

                tvSongName.setText(songName);

                if (albumID != 0) {
                    bmp = getAlbumart(albumID);
                    songIcon.setImageBitmap(bmp);
                }

                if (bmp == null) {
                    Drawable drawable = getResources().getDrawable(R.drawable.music);
                    songIcon.setImageDrawable(null);
                    songIcon.setImageDrawable(drawable);
                }


                bar.setProgress(0);
                bar.setMax(MusicService.mediaPlayer.getDuration());

                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                Intent broadcastIntent = new Intent("udit.updateButtonStateFromNowPlaying");
                broadcastIntent.putExtra("position", position);
                getContext().sendBroadcast(broadcastIntent);
//                seekUpdation();

//                new Thread(NowPlaying.this).start();

//                if (thread != null && thread.isAlive()) {
//                    thread.interrupt();
//                }
////                Toast.makeText(getActivity(),"Now Playing",Toast.LENGTH_LONG).show();
//                thread = new Thread(NowPlaying.this);
//                thread.start();

            }
        });
        setupBroadCast();
        return view;
    }



    public void seekUpdation() {
        if(bar!=null &&MusicService.mediaPlayer!=null ) {
            bar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
            seekHandler.postDelayed(run, 1000);
        }
    }


    private void setupBroadCast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bitmap bmp = null;
                position = intent.getIntExtra("position", 0);
                //        filePath = intent.getStringExtra("filePath");
                bar.setMax(MusicService.mediaPlayer.getDuration());
                songName = AllSongs.audioFilesList.get(position).getaName();
                tvSongName.setText(songName);

                albumID = AllSongs.audioFilesList.get(position).getAlbumID();

                if (albumID != 0) {
                    bmp = getAlbumart(albumID);
                    songIcon.setImageDrawable(null);
                    songIcon.setImageBitmap(bmp);
                }

                if (bmp == null) {
                    Drawable drawable = getResources().getDrawable(R.drawable.music);
                    songIcon.setImageDrawable(null);
                    songIcon.setImageDrawable(drawable);
                }
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
//                seekUpdation();
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
        Bitmap bmp = null;
        position = intent.getIntExtra("position", 0);
        position = position + 1;
        songName = AllSongs.audioFilesList.get(position).getaName();
        tvSongName.setText(songName);
        albumID = AllSongs.audioFilesList.get(position).getAlbumID();

        if (albumID != 0) {
            bmp = getAlbumart(albumID);
            songIcon.setImageDrawable(null);
            songIcon.setImageBitmap(bmp);
        }

        if (bmp == null) {
            Drawable drawable = getResources().getDrawable(R.drawable.music);
            songIcon.setImageDrawable(null);
            songIcon.setImageDrawable(drawable);
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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            if (MusicService.mediaPlayer.isPlaying() || MusicService.mediaPlayer != null) {
                if (fromUser)
                    seekBar.setMax(MusicService.mediaPlayer.getDuration());
                MusicService.mediaPlayer.seekTo(progress);
            } else if (MusicService.mediaPlayer == null) {
                Toast.makeText(getContext(), "Media is not running",
                        Toast.LENGTH_SHORT).show();
                seekBar.setProgress(0);
            }
        } catch (Exception e) {
            Log.e("seek bar", "" + e);
//            seekBar.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void run() {

    }

}