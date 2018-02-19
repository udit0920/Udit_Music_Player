package udit.setia.music.player;

/**
 * Created by uditsetia on 24/1/18.
 */

public class PlayListModel {

    String songName, artistName, songPath, songDuration;


    public PlayListModel(String songName, String artistName, String songPath, String songDuration) {


    }


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }


}
