package com.Showman.apps.moviegallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Showman.apps.moviegallery.model.Reviews;
import com.Showman.apps.moviegallery.utils.SetupSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMovieOverview;
    private ImageView mMoviePoster;
    private ImageView mMovieBackground;
    private RatingBar mMovieRatingStars;
    private TextView mMovieRatingNumber;
    private TextView mOverViewLabel;
    private LinearLayout movieDetailsBox;
    private WebView youTubeVideo;
    private TextView movieGenres;
    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieTitle = findViewById(R.id.movie_details_title);
        mMovieOverview = findViewById(R.id.movie_details_overview);
        mMoviePoster = findViewById(R.id.movie_details_poster);
        movieGenres = findViewById(R.id.movie_details_genre);
        mMovieBackground = findViewById(R.id.movie_background);

        reviewsRecyclerView = findViewById(R.id.reviews);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.hasFixedSize();
        adapter = new ReviewsAdapter(this);
        reviewsRecyclerView.setAdapter(adapter);
        int activityOrientationStatue = this.getResources().getConfiguration().orientation;
        if (activityOrientationStatue == Configuration.ORIENTATION_PORTRAIT) {
            this.mMovieBackground.requestLayout();
            this.mMovieBackground.getLayoutParams().height = 650;
        } else if (activityOrientationStatue == Configuration.ORIENTATION_LANDSCAPE) {
            this.mMovieBackground.requestLayout();
            this.mMovieBackground.getLayoutParams().height = 270;
        }

        mMovieReleaseDate = findViewById(R.id.movie_details_release_date);
        mMovieRatingStars = findViewById(R.id.movie_details_rating_bar);
        mMovieRatingStars.setNumStars(6);
        mMovieRatingStars.setMax(10);
        mMovieRatingStars.setStepSize(0.1f);
        mMovieRatingNumber = findViewById(R.id.movie_details_rating_number);
        mOverViewLabel = findViewById(R.id.overview_label);
        movieDetailsBox = findViewById(R.id.movie_details_box);
        youTubeVideo = findViewById(R.id.trailer);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        setupTheme();

        Intent openedIntent = getIntent();
        if (openedIntent != null) {
            if (openedIntent.hasExtra("movie_title")) {
                mMovieTitle.setText(openedIntent.getStringExtra("movie_title").trim());
            }
            if (openedIntent.hasExtra("movie_release_date")) {
                mMovieReleaseDate.setText(openedIntent.getStringExtra("movie_release_date").trim());
            }
            if (openedIntent.hasExtra("movie_overview")) {
                mMovieOverview.setText(openedIntent.getStringExtra("movie_overview"));
            }
            if (openedIntent.hasExtra("movie_poster_url")) {
                String posterURL = openedIntent.getStringExtra("movie_poster_url");
                Picasso.get().load(posterURL).fit().into(mMoviePoster);
            }
            if (openedIntent.hasExtra("movie_background_url")) {
                String backgroundURL = openedIntent.getStringExtra("movie_background_url");
                Picasso.get().load(backgroundURL).fit().into(mMovieBackground);
            }
            if (openedIntent.hasExtra("movie_rating")) {
                float rating = openedIntent.getFloatExtra("movie_rating", 0.0f);
                String ratingToShow = rating + "/10.0";
                mMovieRatingNumber.setText(ratingToShow);
                mMovieRatingStars.setRating(rating * 6 / 10);
            }
            if (openedIntent.hasExtra("movie_trailer")) {
                String trailerUri = openedIntent.getStringExtra("movie_trailer");
                youTubeVideo.getSettings().setJavaScriptEnabled(true);
                youTubeVideo.setWebChromeClient(new WebChromeClient() {

                } );
                //Log.e("url",trailerUri);
                String videoURL = "<iframe width=\"100%\" height=\"100%\" src=\"" + trailerUri + "\" frameborder=\"0\" allowfullscreen></iframe>";
                youTubeVideo.loadData(videoURL, "text/html" , "utf-8");
            }

            if (openedIntent.hasExtra("movie_genres")) {
                String genres = openedIntent.getStringExtra("movie_genres");
                movieGenres.setText(genres);
            } if (openedIntent.hasExtra("movie_reviews")) {
                ArrayList<Reviews> reviews = openedIntent.getParcelableArrayListExtra("movie_reviews");
                //Log.e("Details Activity", reviews.size()+"");
                adapter.setReviews(reviews);
            }
        }

    }

    private void setupTheme() {
        String theme = SetupSharedPreference.setupTheme(this);
        if (theme.equals(getString(R.string.theme_light_value))) {
            mMovieRatingNumber.setTextColor(getResources().getColor(R.color.movie_details_rating_number_light));
            mOverViewLabel.setTextColor(getResources().getColor(R.color.overview_fixed_color_light));
            mMovieOverview.setTextColor(getResources().getColor(R.color.movie_details_overview_light));
            movieDetailsBox.setBackgroundColor(getResources().getColor(R.color.details_background_light));
        } else if (theme.equals(getString(R.string.theme_dark_value))) {
            mMovieRatingNumber.setTextColor(getResources().getColor(R.color.movie_details_rating_number_dark));
            mOverViewLabel.setTextColor(getResources().getColor(R.color.overview_fixed_color_dark));
            mMovieOverview.setTextColor(getResources().getColor(R.color.movie_details_overview_dark));
            movieDetailsBox.setBackgroundColor(getResources().getColor(R.color.details_background_dark));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.select_theme_list_key))) {
            setupTheme();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }
}
