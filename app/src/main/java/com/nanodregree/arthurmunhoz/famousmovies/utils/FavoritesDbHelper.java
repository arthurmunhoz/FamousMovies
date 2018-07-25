package com.nanodregree.arthurmunhoz.famousmovies.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arthur on 31/03/2018.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper
{
    // Declaring variables
    private static final String DATABSE_NAME = "favoritesList.db";
    private static final int DAABSE_VERSION = 1;

    // Constructor
    public FavoritesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    // OnCreate
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_FAVORITESLIST_TABLE = "CREATE TABLE" +
                FavoritesContract.TABLE_NAME + "(" +
                FavoritesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.COLUMN_ID + "INTEGER NOT NULL," +
                FavoritesContract.COLUMN_TITLE + "TEXT NOT NULL," +
                FavoritesContract.COLUMN_RUNTIME + "INTEGER," +
                FavoritesContract.COLUMN_OVERVIEW + "TEXT," +
                FavoritesContract.COLUMN_POPULARITY + "INTEGER," +
                FavoritesContract.COLUMN_POSTER_PATH + "TEXT," +
                FavoritesContract.COLUMN_BACKDROP_PATH + "TEXT," +
                FavoritesContract.COLUMN_REVENUE + "INTEGER NOT NULL," +
                FavoritesContract.COLUMN_TAGLINE + "TEXT," +
                FavoritesContract.COLUMN_VIDEO + "BOOLEAN NOT NULL," +
                FavoritesContract.COLUMN_VOTE_AVERAGE + "INTEGER NOT NULL," +
                FavoritesContract.COLUMN_VOTE_COUNT + "INTEGER NOT NULL," +
                FavoritesContract.COLUMN_RELEASE_DATE + "TEXT NOT NULL," +
                FavoritesContract.COLUMN_GENRES + "TEXT NOT NULL," +
                FavoritesContract.COLUMN_BUDGET + "INTEGER NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITESLIST_TABLE);


    }

    // OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
