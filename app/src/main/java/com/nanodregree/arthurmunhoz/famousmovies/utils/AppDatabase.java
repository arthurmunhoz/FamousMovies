package com.nanodregree.arthurmunhoz.famousmovies.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nanodregree.arthurmunhoz.famousmovies.model.Movie;
import com.nanodregree.arthurmunhoz.famousmovies.model.MovieDAO;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract MovieDAO movieDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movie_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
