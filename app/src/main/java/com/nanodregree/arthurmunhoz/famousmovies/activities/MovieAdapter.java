package com.nanodregree.arthurmunhoz.famousmovies.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nanodregree.arthurmunhoz.famousmovies.R;
import com.nanodregree.arthurmunhoz.famousmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 27/02/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    private MovieAdapterOnClickHandler mClickHandler;
    private static String POSTER_BASE_PATH = "http://image.tmdb.org/t/p/w342/";

    List<Movie> moviesList = new ArrayList<>();
    Context context;
    Movie movie = new Movie();
    Picasso.Builder builder;

    /************************
            CONSTRUCTOR
    *************************/
    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler)
    {
        this.context = context;
        builder = new Picasso.Builder(context);
        mClickHandler = clickHandler;

        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

            }
        });
    }

    /********************************
        Interface for item clicks
    *********************************/
    public interface MovieAdapterOnClickHandler
    {
        void onClick(Movie movie);
    }

    /*************************
            VIEW HOLDER
    **************************/
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final ImageView mIvMovieItem;

        public MovieViewHolder(View itemView)
        {
            super(itemView);
            mIvMovieItem = (ImageView) itemView.findViewById(R.id.iv_moviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            Movie movie = moviesList.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    public void setMovieData(List<Movie> movieDataList)
    {
        moviesList = movieDataList;
        notifyDataSetChanged();
    }

    /*
            ON CREATE VIEW HOLDER
    */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context c = parent.getContext();
        int layoutId = R.layout.movie_item;
        View view = LayoutInflater.from(c).inflate(layoutId, parent, false);

        return new MovieViewHolder(view);
    }

    /*
            ON BIND VIEW HOLDER
    */
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, int position)
    {
        movie = moviesList.get(position);

        Picasso.get().load(POSTER_BASE_PATH + movie.getPosterPath())
                .resize(320,440)
                .onlyScaleDown()
                .into(viewHolder.mIvMovieItem);
    }

    /*
            GET ITEM COUNT
    */
    @Override
    public int getItemCount()
    {
        if (moviesList != null)
        {
            return moviesList.size();
        }

        return 0;
    }

}
