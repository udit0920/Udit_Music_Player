package com.abhiandroid.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by uditsetia on 24/1/18.
 */

public class DbHelper extends SQLiteOpenHelper {

	public static DbHelper dbHelper;
	public Context context;
	String TABLE_ALL_PLAYLIST = "ALL_PLAYLISTS";
	String TABLE_NAME = "PLAYLISTS";
	String COLUMN_SONG_NAME = "SONG_NAME";
	String COLUMN_ARTIST_NAME = "ARTIST_NAME";
	String COLUMN_SONG_DURATION = "SONG_DURATION";
	String COLUMN_SONG_PATH = "SONG_PATH";
	String COLUMN_PLAYLIST_NAME = "PlayListName";
	String COLUMN_PLAYLIST_NAME2 = "PlayListName";
	String COLUMN_ALBUM_ID = "ALBUM_ID";

	String CREATE_PLAYLIST_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_PLAYLIST_NAME2 + " TEXT, " + COLUMN_SONG_NAME + " TEXT," + COLUMN_ARTIST_NAME + " TEXT,"
			+ COLUMN_SONG_DURATION + " INTEGER," + COLUMN_ALBUM_ID + " INTEGER," + COLUMN_SONG_PATH + " TEXT)";

	String CREATE_ALL_PLAYLISTS_TABLE_QUERY = "CREATE TABLE " + TABLE_ALL_PLAYLIST + " ( " + COLUMN_PLAYLIST_NAME + " TEXT" + ")";

	public DbHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate (SQLiteDatabase db) {

		db.execSQL(CREATE_PLAYLIST_TABLE_QUERY);
		db.execSQL(CREATE_ALL_PLAYLISTS_TABLE_QUERY);

	}

	public static DbHelper getInstance (Context context) {

		if (dbHelper == null) {
			dbHelper = new DbHelper(context, "PlayLists", null, 1);
		}
		return dbHelper;

	}

	public void savePlayList (String playListName, ArrayList<AudioModel> listData) {


		Log.d(TAG, "savePlayList: " + playListName);
		Toast.makeText(context, "Important    " + playListName + "   SIZE " + listData.size(), Toast.LENGTH_LONG).show();

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_PLAYLIST_NAME, playListName);
		db.insert(TABLE_ALL_PLAYLIST, null, cv);

		for (int i = 0; i < listData.size(); i++) {
			//			Toast.makeText(context,"list ",Toast.LENGTH_LONG).show();
			ContentValues cv_2 = new ContentValues();
			cv_2.put(COLUMN_PLAYLIST_NAME2, playListName);
			cv_2.put(COLUMN_ARTIST_NAME, listData.get(i).getaArtist());
			cv_2.put(COLUMN_SONG_DURATION, listData.get(i).getDuration());
			cv_2.put(COLUMN_SONG_PATH, listData.get(i).getaPath());
			cv_2.put(COLUMN_SONG_NAME, listData.get(i).getaName());
			cv_2.put(COLUMN_ALBUM_ID, listData.get(i).getAlbumID());
			long x = db.insert(TABLE_NAME, null, cv_2);
			Toast.makeText(context, "x = " + x, Toast.LENGTH_LONG).show();

		}

		Intent intent = new Intent("udit.update.playLists");
		context.sendBroadcast(intent);
		Toast.makeText(context, "Receiver sent", Toast.LENGTH_SHORT).show();

	}


	public ArrayList<String> getAllPlayLists () {

		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT* FROM " + TABLE_ALL_PLAYLIST;
		Cursor cursor = db.rawQuery(query, null);

		ArrayList<String> output = new ArrayList<>();

		if (cursor.moveToFirst()) {
			do {
				output.add(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYLIST_NAME)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return output;

	}

	public ArrayList<AudioModel> getPlayListSongs (String playListName) {

		SQLiteDatabase db = this.getWritableDatabase();

		String query = "SELECT* FROM " + TABLE_NAME + " WHERE " + COLUMN_PLAYLIST_NAME2 + "=" + "'" + playListName + "'";

		Cursor cursor = db.rawQuery(query, null);

		ArrayList<AudioModel> output = new ArrayList<>();
		Toast.makeText(context, "Size of playList " + playListName + cursor.getCount(), Toast.LENGTH_SHORT).show();
		if (cursor.moveToFirst()) {
			do {
				AudioModel model = new AudioModel();
				model.setaArtist(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)));
				model.setaDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_DURATION)));
				model.setaName(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME)));
				model.setaPath(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_PATH)));
				model.setAlbumID(cursor.getInt(cursor.getColumnIndex(COLUMN_ALBUM_ID)));
				output.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return output;

	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
