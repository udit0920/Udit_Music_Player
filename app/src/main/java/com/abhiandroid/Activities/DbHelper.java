package com.abhiandroid.Activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by uditsetia on 24/1/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static DbHelper dbHelper;

    String TABLE_NAME = "PLAYLISTS";
    String COLUMN_SONG_NAME = "SONG_NAME";
    String COLUMN_ARTIST_NAME = "ARTIST_NAME";
    String COLUMN_SONG_DURATION = "SONG_DURATION";
    String COLUMN_SONG_PATH = "SONG_PATH";

    String CREATE_PLAYLIST_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_SONG_NAME + " TEXT," + COLUMN_ARTIST_NAME + " TEXT,"
            + COLUMN_SONG_DURATION + " INTEGER," + COLUMN_SONG_PATH + " TEXT)";


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PLAYLIST_TABLE_QUERY);

    }

    public static DbHelper getInstance(Context context) {

        if (dbHelper == null) {
            dbHelper = new DbHelper(context, "PlayLists", null, 1);
        }
        return dbHelper;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
