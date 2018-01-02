package com.abhiandroid.Activities;

import android.graphics.Bitmap;

/**
 * Created by uditsetia on 26/12/17.
 */
public class AudioModel {

    String aPath;
    String aName;
    String aAlbum;
    String aArtist;
    String duration;
    int albumID;
    int btnState = -1;


    public String getaPath() {
        return aPath;
    }

    public void setaPath(String aPath) {
        this.aPath = aPath;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public void setAlbumID(int albumID){
        this.albumID = albumID;
    }

    public int getAlbumID(){
        return albumID;
    }

    public void setButtonState(int btnState){
        this.btnState = btnState;
    }

    public int getButtonState(){

        if(btnState!=-1){
            return btnState;
        }
        return 0;

    }

    public void setaDuration(String duration){
        this.duration = duration;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaArtist() {
        return aArtist;
    }

    public String getDuration(){
        return duration;
    }

    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }
}