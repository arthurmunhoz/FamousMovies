package com.nanodregree.arthurmunhoz.famousmovies.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

//DATA ACCESS OBJECT
@Dao
public interface MovieDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table WHERE id IN (:movieID)")
    Movie findById(String movieID);

    @Query("SELECT * FROM movie_table")
    List<Movie> getAllMovies();

    @Query("DELETE FROM movie_table")
    void deleteAll();
}
