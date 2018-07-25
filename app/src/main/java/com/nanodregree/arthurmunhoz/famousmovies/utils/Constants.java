package com.nanodregree.arthurmunhoz.famousmovies.utils;

import com.nanodregree.arthurmunhoz.famousmovies.BuildConfig;

/**
 * Created by Arthur on 30/03/2018.
 *
 * Descriptions:
 *  - This class is used to hold all the strings this app needs.
 */

public class Constants
{
    //URLs
    public static final String POPULAR_MOVIES_URL = Constants.MOVIE_BASE_URL_START
            + Constants.POPULAR
            + Constants.MOVIE_BASE_END
            + Constants.PAGE;

    public static final String TOP_RATED_MOVIES_URL = Constants.MOVIE_BASE_URL_START
            + Constants.TOP_RATED
            + Constants.MOVIE_BASE_END
            + Constants.PAGE;
    private static final String API_KEY            = BuildConfig.API_KEY;
    public static final String KEY_RECYCLER_STATE  = "recycler_state";
    public static final String CATEGORY_CONTROL    = "category_state";
    public static final String POPULAR             = "popular";
    public static final String TOP_RATED           = "top_rated";
    public static final String BACKDROP_PATH       = "http://image.tmdb.org/t/p/w780/";
    public static final String BASE_POSTER_PATH    = "http://image.tmdb.org/t/p/w342/";
    public static final String MOVIE_BASE_URL_START = "https://api.themoviedb.org/3/movie/";
    public static final String MOVIE_BASE_END      = "?api_key=" + API_KEY;
    public static final String PAGE = "&page=";

    public static final String TRAILER_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    //Titles
    public static final String TITLE_POPULAR = "Populares";
    public static final String TITLE_TOP_RATED = "Mais Votados";
    public static final String FAVORITES           = "Meus Filmes Favoritos";
}
