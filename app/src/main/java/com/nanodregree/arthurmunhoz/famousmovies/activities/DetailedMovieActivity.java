package com.nanodregree.arthurmunhoz.famousmovies.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mostafaaryan.transitionalimageview.TransitionalImageView;
import com.mostafaaryan.transitionalimageview.model.TransitionalImage;
import com.nanodregree.arthurmunhoz.famousmovies.R;
import com.nanodregree.arthurmunhoz.famousmovies.utils.AppDatabase;
import com.nanodregree.arthurmunhoz.famousmovies.utils.Constants;
import com.nanodregree.arthurmunhoz.famousmovies.model.Movie;
import com.nanodregree.arthurmunhoz.famousmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DetailedMovieActivity extends AppCompatActivity {

    /**********************
       DECLARING VARIABLES
    ***********************/
    LinearLayout ll_contentFrame;
    ImageView iv_frameBg;
    TextView tv_movieOverview;
    TextView tv_movieTitle;
    TextView tv_movieVoteCount;
    TextView tv_moviePopularity;
    TextView tv_movieGenre;
    TextView tv_movieReleaseDate;
    TextView tv_movieDuration;
    TextView tv_trailers_title;
    TextView tv_reviews_title;
    LinearLayout separator_trailers;
    LinearLayout separator_reviews;
    RelativeLayout rl_main_frame;
    ScrollView sv_scrollContent;
    HorizontalScrollView hsv_trailers;
    LinearLayout ll_trailers;
    HorizontalScrollView hsv_reviews;
    LinearLayout ll_reviews;
    ImageView iv_star;
    TransitionalImageView iv_moviePoster;

    Context context;
    ActionBar actionBar;
    private String FULL_POSTER_PATH = "";
    Movie movie;
    Movie movieFromDb;
    AppDatabase db;

    /**********************
            ON CREATE
    ***********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);

        context = this;

        //Instantiating database object
        db = AppDatabase.getInstance(context);

        attachUI();
        setButtonClicks();

        /* CONFIGURING UI */
        //Changing the ActionBar color and options menu icon
        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }

        //Getting movie object from MainActivity
        if (getIntent().getExtras() != null)
        {
            movie = getIntent().getParcelableExtra("movie");
            Log.v("DetailedMovieActivity ", "MovieID: " + movie.getId());

            checkFavorite();
            loadMovie();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private synchronized void checkFavorite()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                movieFromDb = db.movieDAO().findById(movie.getId());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                if (movieFromDb == null)
                {
                    iv_star.setBackground(getDrawable(R.drawable.star));
                }
                else
                {
                    movie.setFavorite(true);
                    iv_star.setBackground(getDrawable(R.drawable.star_filled));
                }
            }
        }.execute();
    }

    /*************************
        SET BUTTON CLICKS
    *************************/
    private void setButtonClicks()
    {
        iv_star.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v)
            {
                if (!movie.isFavorite())
                {
                    iv_star.setBackground(getDrawable(R.drawable.star_filled));

                    new AsyncTask<Void, Void, Void>()
                    {
                        @Override
                        protected Void doInBackground(Void... voids)
                        {
                            movie.setFavorite(true);
                            db.movieDAO().insert(movie);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid)
                        {
                            super.onPostExecute(aVoid);

                            Snackbar.make(ll_contentFrame, movie.getTitle() + " foi adicionado à lista de favoritos", Snackbar.LENGTH_LONG).show();
                        }
                    }.execute();
                }
                else
                {
                    iv_star.setBackground(getDrawable(R.drawable.star));

                    new AsyncTask<Void, Void, Void>()
                    {
                        @Override
                        protected Void doInBackground(Void... voids)
                        {
                            movie.setFavorite(false);
                            db.movieDAO().delete(movie);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid)
                        {
                            super.onPostExecute(aVoid);

                            Snackbar.make(ll_contentFrame, movie.getTitle() + " foi removido lista de favoritos", Snackbar.LENGTH_LONG).show();
                        }
                    }.execute();
                }
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void loadMovie()
    {
        /* POSTER */
        FULL_POSTER_PATH = Constants.BASE_POSTER_PATH + movie.getPosterPath();
        Picasso.get().load(FULL_POSTER_PATH).fit().into(iv_moviePoster);

        /* BACKDROP IMAGE */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Picasso.get().load(Constants.BACKDROP_PATH + movie.getBackdropPath()).resize(width, height).centerInside().into(iv_frameBg);

        /* TITLE */
        tv_movieTitle.setText(movie.getTitle());
        Log.v("TITLE: ", movie.getTitle());

        /* OVERVIEW */
        tv_movieOverview.setText(movie.getOverview());
        Log.v("OVERVIEW: ", movie.getOverview());

        /* POPULARITY */
        tv_moviePopularity.setText(movie.getVoteAverage());
        Log.v("POPULARITY: ", movie.getVoteAverage());

        /* RELEASE DATE */
        tv_movieReleaseDate.setText(movie.getReleaseDate());
        Log.v("RELEASE_DATE: ", movie.getReleaseDate());

        /* VOTE COUNT */
        tv_movieVoteCount.setText(movie.getVoteCount());

        /* RUNTIME */
        if (movie.getRuntime() == null)
        {
            tv_movieDuration.setText("-");
        }
        else
        {
            tv_movieDuration.setText(movie.getRuntime() + " min");
        }

        /* GENRES */
        tv_movieGenre.setText(movie.getGenreIds());
        Log.v("MOVIE_GENRES: ", movie.getGenreIds()+"");

        /* TRAILERS */
        getTrailers();

        /* REVIEWS */
        getReviews();

        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final Bitmap bitmap = Picasso.get().load(FULL_POSTER_PATH).get();
                    Log.v("ON IMAGE CLICK", "POSTER PATH: " + FULL_POSTER_PATH);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                                    .duration(500)
                                    .backgroundColor(ContextCompat.getColor(context, R.color.black))
                                    .image(bitmap)
                                    .create();
                            iv_moviePoster.setTransitionalImage(transitionalImage);
                        }
                    });
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**********************
            ATTACH UI
     ***********************/
    public void attachUI()
    {
        tv_movieGenre= findViewById(R.id.tv_movieGenre);
        tv_movieOverview = findViewById(R.id.tv_movieOverview);
        tv_movieTitle = findViewById(R.id.tv_movieTitle);
        tv_movieVoteCount = findViewById(R.id.tv_movieVoteCount);
        tv_moviePopularity = findViewById(R.id.tv_moviePopularity);
        tv_movieReleaseDate = findViewById(R.id.tv_movieReleaseDate);
        iv_moviePoster = findViewById(R.id.iv_moviePoster);
        rl_main_frame = findViewById(R.id.rl_main_frame);
        sv_scrollContent = findViewById(R.id.sv_scrollContent);
        iv_frameBg = findViewById(R.id.iv_frameBg);
        ll_contentFrame = findViewById(R.id.ll_contentFrame);
        tv_movieDuration = findViewById(R.id.tv_movieDuration);
        iv_star = findViewById(R.id.iv_star);
        ll_trailers = findViewById(R.id.ll_trailers);
        hsv_trailers = findViewById(R.id.hsv_trailers);
        ll_reviews = findViewById(R.id.ll_reviews);
        hsv_reviews = findViewById(R.id.hsv_reviews);
        separator_reviews = findViewById(R.id.separator_reviews);
        separator_trailers = findViewById(R.id.separator_trailers);
        tv_reviews_title = findViewById(R.id.tv_reviews_title);
        tv_trailers_title = findViewById(R.id.tv_trailers_title);
        //tv_movieOriginalTitle = findViewById(R.id.tv_movieOriginalTitle);
        //tv_movieLanguage = findViewById(R.id.tv_movieLanguage);
        //tv_movieId = findViewById(R.id.tv_movieId);
    }

    /********************************
               GET TRAILERS
     ********************************/
    @SuppressLint("StaticFieldLeak")
    private void getTrailers()
    {
        //Local Variables
        final JSONArray[] jsonArray = {new JSONArray()};
        final String url = Constants.MOVIE_BASE_URL_START
                            + movie.getId()
                            + "/videos"
                            + Constants.MOVIE_BASE_END;

        if (!NetworkUtils.isConnectionAvailable(context))
        {
            NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
        }
        else
        {
            new AsyncTask<Void, Void, Void>()
            {
                View viewAux;

                @Override
                protected Void doInBackground(Void... voids)
                {
                    if (Looper.getMainLooper() == null)
                    {
                        Looper.prepare();
                    }

                    jsonArray[0] = NetworkUtils.makeHttpRequest(context, url, ll_contentFrame);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    if (jsonArray[0].length() == 0)
                    {
                        tv_trailers_title.setVisibility(View.GONE);
                        separator_trailers.setVisibility(View.GONE);
                        return;
                    }

                    if (jsonArray[0] != null)
                    {
                        for (int i = 0; i < jsonArray[0].length(); i++)
                        {
                            viewAux = getLayoutInflater().inflate(R.layout.trailer_item_layout, null);
                            ImageView iv_trailer = viewAux.findViewById(R.id.iv_trailer);
                            TextView tv_trailer_title = viewAux.findViewById(R.id.tv_trailer_title);

                            try
                            {
                                final JSONObject object = jsonArray[0].getJSONObject(i);

                                tv_trailer_title.setText(object.optString("name"));

                                Picasso.get().load(Constants.TRAILER_THUMBNAIL_BASE_URL
                                                    + object.optString("key")
                                                    + "/1.jpg")
                                        .into(iv_trailer);

                                viewAux.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        String youtubeUrl = Constants.YOUTUBE_BASE_URL
                                                            + object.optString("key");
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(youtubeUrl));
                                        startActivity(i);
                                    }
                                });
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            ll_trailers.addView(viewAux);
                        }
                    }
                    else
                    {
                        NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
                    }
                }
            }.execute();
        }
    }

    /********************************
               GET REVIEWS
     ********************************/
    @SuppressLint("StaticFieldLeak")
    private void getReviews()
    {
        //Local Variables
        final JSONArray[] jsonArray = {new JSONArray()};
        final String url = Constants.MOVIE_BASE_URL_START
                + movie.getId()
                + "/reviews"
                + Constants.MOVIE_BASE_END;

        if (!NetworkUtils.isConnectionAvailable(context))
        {
            NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
        }
        else
        {
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... voids)
                {
                    if (Looper.getMainLooper() == null)
                    {
                        Looper.prepare();
                    }

                    jsonArray[0] = NetworkUtils.makeHttpRequest(context, url, ll_contentFrame);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    if (jsonArray[0].length() == 0)
                    {
                        tv_reviews_title.setVisibility(View.GONE);
                        separator_reviews.setVisibility(View.GONE);
                        return;
                    }

                    if (jsonArray[0] != null)
                    {
                        View viewAux;

                        if (jsonArray[0].length() == 0)
                        {
                            viewAux = getLayoutInflater().inflate(R.layout.review_item_layout, null);
                            TextView tv_review_author = viewAux.findViewById(R.id.tv_review_author);
                            tv_review_author.setText("Não há avaliações para este filme.");
                            ll_reviews.addView(viewAux);

                            return;
                        }

                        for (int i = 0; i < jsonArray[0].length(); i++)
                        {
                            viewAux = getLayoutInflater().inflate(R.layout.review_item_layout, null);
                            TextView tv_review_author_content = viewAux.findViewById(R.id.tv_review_author_content);
                            TextView tv_review_content_text = viewAux.findViewById(R.id.tv_review_content_text);

                            try
                            {
                                final JSONObject object = jsonArray[0].getJSONObject(i);

                                tv_review_author_content.setText(object.optString("author"));
                                tv_review_content_text.setText(object.optString("content"));

                                viewAux.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        String youtubeUrl = "https://www.themoviedb.org/review/"
                                                + object.optString("id");
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(youtubeUrl));
                                        startActivity(i);
                                    }
                                });
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            ll_reviews.addView(viewAux);
                        }
                    }
                    else
                    {
                        NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
                    }
                }
            }.execute();
        }
    }
}
