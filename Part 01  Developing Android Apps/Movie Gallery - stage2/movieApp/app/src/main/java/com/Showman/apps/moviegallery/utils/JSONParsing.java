package com.Showman.apps.moviegallery.utils;

import android.util.Log;

import com.Showman.apps.moviegallery.AppExecutors;
import com.Showman.apps.moviegallery.model.Movie;
import com.Showman.apps.moviegallery.model.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class JSONParsing {

    public static final int GET_DETAILS_ALSO = 100;
    public static final int DO_NOT_GET_DETAILS = 200;

     public static ArrayList<Movie> getMoviesListFromJSON(String JSONMoviesCategory, int statue) {
         if (JSONMoviesCategory == null) return null;
         ArrayList<Movie> moviesList = new ArrayList<>();
         try {
             JSONObject root = new JSONObject(JSONMoviesCategory);
             JSONArray results = root.optJSONArray("results");
             if (results != null && results.length() > 0) {
                 for (int i = 0; i < results.length(); i++) {
                     JSONObject movieResult = results.getJSONObject(i);
                     final int id = movieResult.optInt("id");
                     String title = movieResult.optString("title");
                     String moviePoster = movieResult.optString("poster_path");
                     String movieBackground = movieResult.optString("backdrop_path");
                     String movieOverview = movieResult.optString("overview");
                     double rating = movieResult.optDouble("vote_average");
                     String releaseDate = movieResult.optString("release_date");
                     final Movie movie = new Movie(id, title, moviePoster, movieBackground, movieOverview, rating, releaseDate);
                     if (statue == GET_DETAILS_ALSO) {
                         movie.setGenres(getGenres(id));
                         AppExecutors.getsInstance().getNetworkIO().execute(new Runnable() {
                             @Override
                             public void run() {
                                 movie.setmTrailerKey(getTrailerKey(id));
                                 movie.setMovieReviews(getReviews(id));
                             }
                         });
                     }
                     moviesList.add(movie);
                 }
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
         return moviesList;
     }


    private static String getTrailerKey(int movieId) {
        URL trailersURL = NetworkUtils.getMovieDetailsURL(NetworkUtils.GET_TRAILER_KEY, movieId);
        if (trailersURL == null) {
            return null;
        }
        String JSONResponse = null;
        try {
            JSONResponse = NetworkUtils.getJSONResponse(trailersURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String trailerKey = null;
        if (JSONResponse != null) {
            try {
                JSONObject root = new JSONObject(JSONResponse);
                JSONArray trailersResults = root.optJSONArray("results");
                if (trailersResults != null && trailersResults.length() > 0) {
                    JSONObject officialTrailer = trailersResults.getJSONObject(0);
                    trailerKey = officialTrailer.getString("key");
                    Log.e("trailer key", trailerKey);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailerKey;
    }

    private static ArrayList<Reviews> getReviews(int movieId) {
         ArrayList<Reviews> reviews = new ArrayList<>();
        URL reviewsURL = NetworkUtils.getMovieDetailsURL(NetworkUtils.GET_REVIEWS, movieId);
        if (reviewsURL == null) {
            return null;
        }
        String JSONResponse = null;
        try {
            JSONResponse = NetworkUtils.getJSONResponse(reviewsURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (JSONResponse != null) {
            try {
                JSONObject root = new JSONObject(JSONResponse);
                JSONArray reviewsResults = root.optJSONArray("results");
                if (reviewsResults != null && reviewsResults.length() > 0) {
                    for (int i = 0; i < reviewsResults.length(); i++) {
                        JSONObject review = reviewsResults.getJSONObject(i);
                        String author = review.getString("author");
                        String content = review.getString("content");
                        reviews.add(new Reviews(author, content));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    private static String[] getGenres(int movieId) {
         String[] genresArray = new String[4];
         URL movieDetailsURL = NetworkUtils.getMovieDetailsURL(NetworkUtils.GET_DETAILS, movieId);
         if (movieDetailsURL == null) return null;

         String JSONResponse = null;

         try {
             JSONResponse = NetworkUtils.getJSONResponse(movieDetailsURL);
         } catch (IOException e) {
             e.printStackTrace();
         }

         if (JSONResponse != null) {
             try {
                 JSONObject root = new JSONObject(JSONResponse);
                 JSONArray movieGenres = root.optJSONArray("genres");
                 if (movieGenres != null && movieGenres.length() > 0) {
                     for (int i = 0; i < movieGenres.length(); i++) {
                         JSONObject genreObject = movieGenres.getJSONObject(i);
                         String genreText = genreObject.getString("name");
                         if (i > 3) break;
                         genresArray[i] = genreText;
                     }
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         return genresArray;
    }
}
