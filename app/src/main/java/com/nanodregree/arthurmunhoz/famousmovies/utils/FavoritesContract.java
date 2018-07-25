package com.nanodregree.arthurmunhoz.famousmovies.utils;

import android.provider.BaseColumns;

/**
 * Created by Arthur on 31/03/2018.
 */

public class FavoritesContract implements BaseColumns
{
    // TABLE NAME -> favoritesList
    public static final String TABLE_NAME = "favoritesList";

    // COLUMN_ID -> id
    public static final String COLUMN_ID = "id";

    // COLUMN_TITLE -> title
    public static final String COLUMN_TITLE = "title";

    // COLUMN_REVENUE -> revenue
    public static final String COLUMN_REVENUE = "revenue";

    // COLUMN_RELEASE_DATE -> releaseDate
    public static final String COLUMN_RELEASE_DATE = "releaseDate";

    // COLUMN_RUNTIME -> runtime
    public static final String COLUMN_RUNTIME = "runtime";

    // COLUMN_OVERVIEW -> overview
    public static final String COLUMN_OVERVIEW = "overview";

    // COLUMN_BACKDROP_PATH -> backdropPath
    public static final String COLUMN_BACKDROP_PATH = "backdropPath";

    // COLUMN_BUDGET -> id
    public static final String COLUMN_BUDGET = "budget";

    // COLUMN_GENRES -> genres
    public static final String COLUMN_GENRES = "genres";

    // COLUMN_POPULARITY -> popularity
    public static final String COLUMN_POPULARITY = "popularity";

    // COLUMN_POSTER_PATH -> posterPath
    public static final String COLUMN_POSTER_PATH = "posterPath";

    // COLUMN_TAGLINE -> tagline
    public static final String COLUMN_TAGLINE = "tagline";

    // COLUMN_VOTE_COUNT -> voteCount
    public static final String COLUMN_VOTE_COUNT = "voteCount";

    // COLUMN_VOTE_AVERAGE -> voteAverage
    public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

    // COLUMN_VIDEO -> video
    public static final String COLUMN_VIDEO = "video";

}
