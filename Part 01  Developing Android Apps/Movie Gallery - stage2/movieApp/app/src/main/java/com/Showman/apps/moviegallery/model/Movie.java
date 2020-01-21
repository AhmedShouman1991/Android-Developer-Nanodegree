package com.Showman.apps.moviegallery.model;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Movie {
    private int mMovieId;
    private String mTitle;
    private String mPosterUrl;
    private String mBackgroundUrl;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;
    private String mTrailerKey;
    private ArrayList<Reviews> movieReviews = new ArrayList<>();
    private String[] genres;
    private final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    private final String MOVIE_BACKGROUND_BASE_URL = "https://image.tmdb.org/t/p/w780/";
    private final String MOVIE_YOUTUBE_TRAILER_BASE_URL = "https://www.youtube.com/embed/";
    public Movie(int mMovieId, String mTitle, String mPosterUrl, String mBackgroundUrl, String mOverview, double mUserRating, String mReleaseDate) {
        this.mMovieId = mMovieId;
        this.mTitle = mTitle;
        this.mPosterUrl = MOVIE_POSTER_BASE_URL + mPosterUrl;
        this.mBackgroundUrl = MOVIE_BACKGROUND_BASE_URL + mBackgroundUrl;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;

    }

    public String getmTitle() {
        return mTitle;
    }

    public URL getmPosterUrl() {
        URL posterURL = null;
        try {
            posterURL = new URL(this.mPosterUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return posterURL;
    }

    public URL getmBackgroundUrl() {
        URL backgroundUrl = null;
        try {
            backgroundUrl = new URL(this.mBackgroundUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return backgroundUrl;
    }

    public String getmOverview() {
        return mOverview;
    }

    public double getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public void setmTrailerKey(String mTrailerKey) {
        this.mTrailerKey = mTrailerKey;
    }

    public void setMovieReviews(ArrayList<Reviews> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public URL getTrailerURL() {
        String mTrailer = this.MOVIE_YOUTUBE_TRAILER_BASE_URL + mTrailerKey;
        URL trailerURL = null;
        try{
            trailerURL = new URL(mTrailer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return trailerURL;
    }

    public String getGenres() {
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i< this.genres.length; i++) {
            if (null != this.genres[i]) {
                if (i > 0) {
                    genres.append(", ").append(this.genres[i]);
                } else {
                    genres.append(this.genres[i]);

                }
            }
        }
        return genres.toString();
    }

    public ArrayList<Reviews> getMovieReviews() {
        return movieReviews;
    }

    @NonNull
    @Override
    public String toString() {

        String result =  this.mTitle + this.getTrailerURL().toString() ;
        for (int i= 0; i < movieReviews.size(); i++) {
            result += (movieReviews.get(i).getAuthor() + movieReviews.get(i).getContent());
        }
        return result;
    }
}
