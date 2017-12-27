package com.abhiandroid.Activities.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by uditsetia on 27/12/17.
 */
public class MusicService extends Service {

    MediaPlayer mediaPlayer;


    public MusicService(){

    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        stopSelf();
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        String PATH_TO_FILE = intent.getStringExtra("file_path");
        mediaPlayer = new  MediaPlayer();
        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        return START_NOT_STICKY;

    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    public void stopMusic(){
        stopSelf();
    }


}
