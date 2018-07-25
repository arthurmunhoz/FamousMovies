package com.nanodregree.arthurmunhoz.famousmovies.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanodregree.arthurmunhoz.famousmovies.R;
import com.nanodregree.arthurmunhoz.famousmovies.utils.AppDatabase;
import com.nanodregree.arthurmunhoz.famousmovies.utils.EndlessRecyclerOnScrollListener;
import com.nanodregree.arthurmunhoz.famousmovies.utils.Constants;
import com.nanodregree.arthurmunhoz.famousmovies.utils.NetworkUtils;
import com.nanodregree.arthurmunhoz.famousmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler
{
    /* Creating variables */
    private RecyclerView mRecyclerViewMoviesList;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private ImageView iv_swipeArrow;
    private TextView tv_swipeToRefresh;
    private SwipeRefreshLayout srl_swipeToRefresh;
    private RelativeLayout rl_main_frame;
    AppDatabase db;

    static List<Movie> moviesList = new ArrayList<>();

    private static Bundle mBundleRecyclerViewState;
    int value;
    GridLayoutManager mGridLayoutManager;
    Context context;
    ActionBar actionBar;
    int pageControl = 1;
    int scrollControl = 1;
    String categoryControl = Constants.TITLE_POPULAR; //app initializes showing "popular movies" first

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting activity context
        context = this;

        //Instantiating database object
        db = AppDatabase.getInstance(context);

        //Attaching UI
        attachUI();

        //Definindo clicks dos botões
        setClicks();

        /* CONFIGURING UI */
        //Changing the ActionBar color and options menu icon
        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.black)));
        }

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));
            window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        }

        //Creating the adapter
        mAdapter = new MovieAdapter(this, this);

        //Specifying the layout manager
        value = this.getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT)
        {
            mGridLayoutManager = new GridLayoutManager(context, 2);
        }
        else if (value == Configuration.ORIENTATION_LANDSCAPE)
        {
            mGridLayoutManager = new GridLayoutManager(context, 4);
        }
        mRecyclerViewMoviesList.setLayoutManager(mGridLayoutManager);

        //Configuring the recyclerView
        mRecyclerViewMoviesList.setHasFixedSize(true);
        mRecyclerViewMoviesList.setVisibility(View.VISIBLE);
        mRecyclerViewMoviesList.setAdapter(mAdapter);

        getMovies(Constants.POPULAR_MOVIES_URL + pageControl, categoryControl);
    }

    /****************
     ON PAUSE
     ****************/
    @Override
    protected void onPause()
    {
        super.onPause();

        //Saving RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerViewMoviesList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(Constants.KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putString(Constants.CATEGORY_CONTROL, categoryControl);
    }

    /****************
     ON RESUME
     ****************/
    @Override
    protected void onResume()
    {
        super.onResume();

        if (mBundleRecyclerViewState != null)
        {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(Constants.KEY_RECYCLER_STATE);
            mRecyclerViewMoviesList.getLayoutManager().onRestoreInstanceState(listState);
            categoryControl = mBundleRecyclerViewState.getString(Constants.CATEGORY_CONTROL);
            actionBar.setTitle(categoryControl);
        }

        if (value == Configuration.ORIENTATION_PORTRAIT)
        {
            mGridLayoutManager = new GridLayoutManager(context, 2);
        }
        else if (value == Configuration.ORIENTATION_LANDSCAPE)
        {
            mGridLayoutManager = new GridLayoutManager(context, 4);
        }

        if (categoryControl.contentEquals(Constants.FAVORITES))
        {
            fetchFavoriteMoviesList();
        }
    }

    /****************
     ATTACH UI
     ****************/
    public void attachUI()
    {
        mRecyclerViewMoviesList = findViewById(R.id.rv_moviesList);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        iv_swipeArrow = findViewById(R.id.iv_swipeArrow);
        tv_swipeToRefresh = findViewById(R.id.tv_swipeToRefresh);
        srl_swipeToRefresh = findViewById(R.id.srl_refreshRecyclerView);
        rl_main_frame = findViewById(R.id.rl_main_frame);
    }

    /***************************
     SET CLICKS ACTIONS
     **************************/
    public void setClicks()
    {
        mRecyclerViewMoviesList.addOnScrollListener(new EndlessRecyclerOnScrollListener()
        {
            @Override
            public void onLoadMore()
            {
                if (categoryControl.contentEquals(Constants.FAVORITES))
                {
                    return;
                }

                pageControl++;

                if (categoryControl.contentEquals(Constants.TITLE_POPULAR))
                {
                    getMovies(Constants.POPULAR_MOVIES_URL + pageControl, categoryControl);
                }
                else
                {
                    getMovies(Constants.TOP_RATED_MOVIES_URL + pageControl, categoryControl);
                }
            }
        });

        srl_swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (categoryControl.contentEquals(Constants.TITLE_POPULAR))
                {
                    if (!NetworkUtils.isConnectionAvailable(context))
                    {
                        srl_swipeToRefresh.setRefreshing(false);
                    }
                    else
                    {
                        getMovies(Constants.POPULAR_MOVIES_URL + pageControl, categoryControl);
                        srl_swipeToRefresh.setRefreshing(false);
                    }
                }
                else if (categoryControl.contentEquals(Constants.TITLE_TOP_RATED))
                {
                    if (!NetworkUtils.isConnectionAvailable(context))
                    {
                        srl_swipeToRefresh.setRefreshing(false);
                    }
                    else
                    {
                        getMovies(Constants.TOP_RATED_MOVIES_URL + pageControl, categoryControl);
                        srl_swipeToRefresh.setRefreshing(false);
                    }
                }
                else if (categoryControl.contentEquals(Constants.FAVORITES))
                {
                    {
                        fetchFavoriteMoviesList();
                        srl_swipeToRefresh.setRefreshing(false);
                    }
                }
            }
        });

    }

    /***************************
     ON VIEW HOLDER CLICK
     ***************************/
    @Override
    public void onClick(Movie movie)
    {
        if (!NetworkUtils.isConnectionAvailable(context))
        {
            NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
        }
        else
        {
            Intent intentToMovieDetails = new Intent(MainActivity.this, DetailedMovieActivity.class);
            intentToMovieDetails.putExtra("movie", movie);
            //intentToMovieDetails.putExtra(Intent.EXTRA_TEXT, movie.getId());
            startActivity(intentToMovieDetails);
        }
    }

    /****************************
     *
     * GET MOVIES
     *
     * @param url = String to represent the url to be requested
     * @param catControl = String used to keep try of the kind of search being made
     */
    @SuppressLint("StaticFieldLeak")
    private void getMovies(String url, final String catControl)
    {
        checkListContent();

        //Declaring variables
        url = url + pageControl;
        final JSONArray[] jsonResultsArray = {new JSONArray()};
        final String finalUrl = url;

        Log.e("URL = ", url);

        //Creating task to fetch movies data
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                //Setting the title for the actionBar
                actionBar.setTitle(catControl);

                categoryControl = catControl;
                tv_swipeToRefresh.setVisibility(View.INVISIBLE);
                iv_swipeArrow.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }

                jsonResultsArray[0] = NetworkUtils.makeHttpRequest(context, finalUrl, rl_main_frame);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                if (jsonResultsArray[0] != null)
                {
                    for (int i = 0; i < jsonResultsArray[0].length(); i++)
                    {
                        Movie movie = new Movie();
                        try
                        {
                            JSONObject object = jsonResultsArray[0].getJSONObject(i);

                            movie.setVoteCount(object.optString("vote_count"));
                            movie.setVoteAverage(object.optString("vote_average"));
                            movie.setAdult(object.optString("adult"));
                            movie.setBackdropPath(object.optString("backdrop_path"));
                            movie.setId(object.optString("id"));
                            movie.setOriginalTitle("original_title");
                            movie.setPosterPath(object.optString("poster_path"));
                            movie.setOverview(object.optString("overview"));
                            movie.setTitle(object.optString("title"));
                            movie.setReleaseDate(object.optString("release_date"));
                            movie.setVideos(object.optBoolean("video"));
                            movie.setPopularity(object.optString("popularity"));

                            Log.v("MainActivity.java", movie.getTitle());

                            moviesList.add(movie);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                //Configuring the UI elements
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                srl_swipeToRefresh.setRefreshing(false);
                if (scrollControl == 1)
                {
                    scrollControl++;
                    mAdapter.setMovieData(moviesList);
                }

                mRecyclerViewMoviesList.getRecycledViewPool().clear();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    /********************************
        FETCH FAVORITE MOVIES LIST
     ********************************/
    @SuppressLint("StaticFieldLeak")
    private void fetchFavoriteMoviesList()
    {
        if (!NetworkUtils.isConnectionAvailable(context))
        {
            Snackbar.make(srl_swipeToRefresh, getString(R.string.error_message), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            new AsyncTask<Void, Void, Void>()
            {
                List<Movie> movieList = new ArrayList<>();

                @Override
                protected Void doInBackground(Void... voids)
                {
                    movieList = db.movieDAO().getAllMovies();

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    if (movieList.size() == 0)
                    {
                        Snackbar.make(rl_main_frame, "Lista de filmes favoritos vazia!", 2500).show();

                        if (categoryControl.contentEquals(Constants.TITLE_TOP_RATED))
                        {
                            getMovies(Constants.TOP_RATED_MOVIES_URL + pageControl, categoryControl);
                        }
                        else if (categoryControl.contentEquals(Constants.TITLE_POPULAR))
                        {
                            getMovies(Constants.POPULAR_MOVIES_URL + pageControl, categoryControl);
                        }
                        else if (categoryControl.contentEquals(Constants.FAVORITES))
                        {
                            fetchFavoriteMoviesList();
                        }

                        return;
                    }

                    actionBar.setTitle(getResources().getString(R.string.favorites));
                    moviesList.clear();
                    mAdapter.setMovieData(movieList);
                    mAdapter.notifyDataSetChanged();
                    categoryControl = Constants.FAVORITES;
                    scrollControl = 1;
                }
            }.execute();
        }
    }

    private void checkListContent()
    {
        if (moviesList.size() == 0)
        {
            tv_swipeToRefresh.setVisibility(View.VISIBLE);

            //Animate swipe arrow
            iv_swipeArrow.setVisibility(View.VISIBLE);
            iv_swipeArrow.animate().translationY(60).setDuration(600).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    iv_swipeArrow.animate().translationY(0).setDuration(600).setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);

                            srl_swipeToRefresh.setRefreshing(false);
                        }
                    }).start();
                }
            }).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_category, menu);
        MenuItem top_rated_item = menu.findItem(R.id.top_rated);
        MenuItem popular_item = menu.findItem(R.id.popular);
        MenuItem favorites_item = menu.findItem(R.id.favorites);

        favorites_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @SuppressLint("StaticFieldLeak")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                fetchFavoriteMoviesList();
                return true;
            }
        });

        top_rated_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                if (!NetworkUtils.isConnectionAvailable(context))
                {
                    Snackbar.make(srl_swipeToRefresh, getString(R.string.error_message), Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    pageControl = 1;
                    categoryControl = Constants.TITLE_TOP_RATED;
                    moviesList.clear();

                    getMovies(Constants.TOP_RATED_MOVIES_URL + pageControl, categoryControl);
                }

                return false;
            }
        });

        popular_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                if (!NetworkUtils.isConnectionAvailable(context))
                {
                    NetworkUtils.showConnectionErrorMsg(context, rl_main_frame);
                }
                else
                {
                    pageControl = 1;
                    categoryControl = Constants.TITLE_POPULAR;
                    moviesList.clear();

                    getMovies(Constants.POPULAR_MOVIES_URL + pageControl, categoryControl);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
