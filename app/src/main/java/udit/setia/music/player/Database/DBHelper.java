package udit.setia.music.player.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import udit.setia.music.player.AudioModel;

/**
 * Created by uditsetia on 07/01/18.
 */

public class DBHelper extends SQLiteOpenHelper {


	String TABLE_NAME = "PLAYLIST";
	String COLUMN_SONG_DURATION = "SONG_DURATION";
	String COLUMN_SONG_NAME = "SONG_NAME";
	String COLUMN_SINGER_NAME = "SINGER_NAME";
	String COLUMN_FILE_PATH = "FILE_PATH";


	String CREATE_TABLE_PLAYLIST = "CREATE TABLE " + TABLE_NAME + "( " +TABLE_NAME+" TEXT, "+COLUMN_SONG_DURATION+" TEXT, "
									+COLUMN_SINGER_NAME+" TEXT, "+COLUMN_FILE_PATH +" TEXT)";

	public DBHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PLAYLIST);
	}

	public void insertIntoPlayList(ArrayList<AudioModel> songsList){





	}



	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
