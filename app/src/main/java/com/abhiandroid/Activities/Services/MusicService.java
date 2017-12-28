package com.abhiandroid.Activities.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by uditsetia on 27/12/17.
 */
public class MusicService extends Service {

    static MediaPlayer mediaPlayer;
    int position;

    public MusicService(){

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new  MediaPlayer();
        Toast.makeText(getBaseContext(),"CHAMP",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        stopSelf();
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        position = intent.getIntExtra("position",0);
        String PATH_TO_FILE = intent.getStringExtra("file_path");

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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                performOnEnd();
            }

        });
        mediaPlayer.start();
        return START_NOT_STICKY;

    }

    private void performOnEnd() {
        Intent intent = new Intent("udit.ButtonState");
        intent.putExtra("btnState",0);
        intent.putExtra("position",position);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
