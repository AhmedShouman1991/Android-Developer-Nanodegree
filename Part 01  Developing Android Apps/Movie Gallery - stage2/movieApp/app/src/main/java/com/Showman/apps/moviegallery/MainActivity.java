package com.Showman.apps.moviegallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.Showman.apps.moviegallery.model.Movie;
import com.Showman.apps.moviegallery.model.Reviews;
import com.Showman.apps.moviegallery.myFavoriteDb.FavoritesDataBase;
import com.Showman.apps.moviegallery.myFavoriteDb.MovieOfflineEntity;
import com.Showman.apps.moviegallery.notificationDb.IdDatabase;
import com.Showman.apps.moviegallery.notificationDb.IdEntry;
import com.Showman.apps.moviegallery.sync.MoviesSyncUtils;
import com.Showman.apps.moviegallery.utils.IdList;
import com.Showman.apps.moviegallery.utils.JSONParsing;
import com.Showman.apps.moviegallery.utils.NetworkUtils;
import com.Showman.apps.moviegallery.utils.SetupSharedPreference;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnGridItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private TextView errorTextView;
    private ProgressBar loadingProgressBar;
    private RecyclerView moviesGridView;
    private MoviesAdapter adapter;
    private OfflineMovieAdapter offlineAdapter;
    private FavoritesDataBase fDb;
    private IdDatabase idDb;
    private boolean isSwipeEnabled = false;


    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fDb = FavoritesDataBase.getInstance(this);
        idDb = IdDatabase.getInstance(this);

        offlineAdapter = new OfflineMovieAdapter(this);

        errorTextView = findViewById(R.id.api_response_error);

        loadingProgressBar = findViewById(R.id.pb_loading_indicator);

        moviesGridView = findViewById(R.id.movies_grid);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        int numberOfColumns = getResources().getInteger(R.integer.min_columns_number_port);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        moviesGridView.setLayoutManager(gridLayoutManager);
        ItemsOffsetDecoration decoration = new ItemsOffsetDecoration(this, R.dimen.item_offset);
        moviesGridView.addItemDecoration(decoration);
        moviesGridView.setHasFixedSize(true);
        adapter = new MoviesAdapter(this, this);
        moviesGridView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return isSwipeEnabled;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = (int) viewHolder.itemView.getTag();
                        List<MovieOfflineEntity> tasks = offlineAdapter.getMovieOfflineList();
                        fDb.movieDAO().deleteMovie(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(moviesGridView);

        MoviesSyncUtils.scheduleFirebaseJobDispatcher(this);
        setupTheme();
        setupViewModel();
        getMoviesByPopular();

    }


    private void setupViewModel() {
        FavoriteViewModel mainViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        mainViewModel.getFavorites().observe(this, new Observer<List<MovieOfflineEntity>>() {
            @Override
            public void onChanged(List<MovieOfflineEntity> movieOfflineEntities) {
                offlineAdapter.setMovieOfflineList(movieOfflineEntities);
            }
        });
    }


    private void setupTheme() {
        String themeValue = SetupSharedPreference.setupTheme(this);

        if (themeValue.equals(getString(R.string.theme_dark_value))) {
            moviesGridView.setBackgroundColor(getResources().getColor(R.color.movies_list_background_color_dark));

        } else if (themeValue.equals(getString(R.string.theme_light_value))) {
            moviesGridView.setBackgroundColor(getResources().getColor(R.color.movies_list_background_color_light));
        }
        moviesGridView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent detailsActivityIntent = new Intent(this, MovieDetailsActivity.class);

        String movieTitle = movie.getmTitle();
        String movieReleaseData = movie.getmReleaseDate();
        String movieOverview = movie.getmOverview();
        String moviePosterURL = movie.getmPosterUrl().toString();
        String movieBackgroundURL = movie.getmBackgroundUrl().toString();
        String movieTrailerURL = movie.getTrailerURL().toString();
        String genres = movie.getGenres().trim();
        float userRating = (float) movie.getmUserRating();
        ArrayList<Reviews> reviews = movie.getMovieReviews();
        //Log.e("main Activity", reviews.size()+"");

        detailsActivityIntent.putExtra("movie_title", movieTitle);
        detailsActivityIntent.putExtra("movie_release_date", movieReleaseData);
        detailsActivityIntent.putExtra("movie_overview", movieOverview);
        detailsActivityIntent.putExtra("movie_poster_url", moviePosterURL);
        detailsActivityIntent.putExtra("movie_background_url", movieBackgroundURL);
        detailsActivityIntent.putExtra("movie_rating", userRating);
        detailsActivityIntent.putExtra("movie_trailer", movieTrailerURL);
        detailsActivityIntent.putExtra("movie_genres", genres);
        detailsActivityIntent.putParcelableArrayListExtra("movie_reviews", reviews);
        startActivity(detailsActivityIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.show_notification_key))) {

        } else if (key.equals(getString(R.string.select_theme_list_key))) {
            setupTheme();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void getMoviesByPopular() {
        isSwipeEnabled = false;
        if (isConnected()) {
            showMoviesList();
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            showErrorMsg();
        }
    }

    private void getMoviesByTopRated() {
        isSwipeEnabled = false;
        URL moviesURL = NetworkUtils.getUrl(NetworkUtils.FIND_BY_TOP_RATED);
        if (isConnected()) {
            showMoviesList();
            getSupportLoaderManager().initLoader(1, null, this);
        } else {
            showErrorMsg();
        }
    }

    private void showMoviesList() {
        errorTextView.setVisibility(View.GONE);
        moviesGridView.setVisibility(View.VISIBLE);
    }

    private void showErrorMsg() {
        moviesGridView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_by_popularity:
                getMoviesByPopular();
                moviesGridView.setAdapter(adapter);
                return true;
            case R.id.action_by_top_rated:
                getMoviesByTopRated();
                moviesGridView.setAdapter(adapter);
                return true;
            case R.id.action_by_favorite:
                isSwipeEnabled = true;
                moviesGridView.setAdapter(offlineAdapter);
                return true;
            case R.id.action_open_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private class ConnectInBackground extends AsyncTask<URL, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            loadingProgressBar.setVisibility(View.VISIBLE);
//            moviesGridView.smoothScrollToPosition(0);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            loadingProgressBar.setVisibility(View.INVISIBLE);
//            if (s != null) {
//                movies = JSONParsing.getMoviesListFromJSON(s);
//                if (movies != null) {
//                    adapter.setMoviesList(movies);
//                    moviesGridView.setAdapter(adapter);
//                }
//            }else {
//                errorTextView.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        protected String doInBackground(URL... urls) {
//            if (urls == null || urls.length == 0) {
//                return null;
//            }
//            URL moviesURL = urls[0];
//            String JSONResponse = null;
//            try {
//                JSONResponse = NetworkUtils.getJSONResponse(moviesURL);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return JSONResponse;
//        }
//    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case 0:
                return new AsyncTaskLoader<List<Movie>>(this) {

                    List<Movie> mMoviesList = null;

                    @Override
                    protected void onStartLoading() {
                        if (mMoviesList != null) {
                            deliverResult(mMoviesList);
                        } else {
                            loadingProgressBar.setVisibility(View.VISIBLE);
                            moviesGridView.smoothScrollToPosition(0);
                            forceLoad();
                        }
                    }

                    @Nullable
                    @Override
                    public List<Movie> loadInBackground() {
                        URL moviesURL = NetworkUtils.getUrl(NetworkUtils.FIND_BY_POPULAR);
                        String JSONMoviesCategory;
                        List<Movie> moviesList = null;
                        try {
                            JSONMoviesCategory = NetworkUtils.getJSONResponse(moviesURL);
                            moviesList = JSONParsing.getMoviesListFromJSON(JSONMoviesCategory, JSONParsing.GET_DETAILS_ALSO);

                            updateNotificaionDb(moviesList);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return moviesList;
                    }

                    public void deliverResult(List<Movie> data) {
                        mMoviesList = data;
                        super.deliverResult(data);
                    }
                };


            case 1:
                return new AsyncTaskLoader<List<Movie>>(this) {
                    List<Movie> mMoviesList = null;

                    @Override
                    protected void onStartLoading() {
                        if (mMoviesList != null) {
                            deliverResult(mMoviesList);
                        } else {
                            loadingProgressBar.setVisibility(View.VISIBLE);
                            moviesGridView.smoothScrollToPosition(0);
                            forceLoad();
                        }
                    }

                    @Nullable
                    @Override
                    public List<Movie> loadInBackground() {
                        URL moviesURL = NetworkUtils.getUrl(NetworkUtils.FIND_BY_TOP_RATED);
                        String JSONMoviesCategory;
                        List<Movie> moviesList = null;
                        try {
                            JSONMoviesCategory = NetworkUtils.getJSONResponse(moviesURL);
                            moviesList = JSONParsing.getMoviesListFromJSON(JSONMoviesCategory, JSONParsing.GET_DETAILS_ALSO);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return moviesList;
                    }

                    @Override
                    public void deliverResult(@Nullable List<Movie> data) {
                        mMoviesList = data;
                        super.deliverResult(data);
                    }
                };
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    private void updateNotificaionDb(List<Movie> moviesList) {
        idDb.idDAO().deleteAll();
        Log.e("ID DATABASE", "DELETED");
        List<IdEntry> ids = IdList.transformToIds(moviesList);
        idDb.idDAO().insert(ids);
        Log.e("ID DATABASE", "All Inserted");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, final List<Movie> data) {
        loadingProgressBar.setVisibility(View.INVISIBLE);

        if (data != null) {
            adapter.setMoviesList(data);
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        adapter.setMoviesList(null);
    }
}
