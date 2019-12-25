package com.Showman.apps.moviegallery.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String MOVIE_DB_URL = "//api.themoviedb.org/3/movie";

    public static final int FIND_BY_POPULAR = 2;

    public static final int FIND_BY_TOP_RATED = 4;

    public static final int GET_TRAILER_KEY = 10;

    public static final int GET_REVIEWS = 20;

    public static final int GET_DETAILS = 30;

    private static final String TRAILERS = "videos";

    private static final String REVIEWS = "reviews";

    private static final String POPULAR = "popular";

    private static final String TOP_RATED = "top_rated";

    private final static String MOVIE_TRAILER = "http://api.themoviedb.org/3/movie/181812/videos?api_key=";

    private static final String API_KEY = "api_key";

    private static final String KEY_VALUE = "";

    public static URL getUrl(int findBy) {

        Uri.Builder builder = Uri.parse(MOVIE_DB_URL).buildUpon();
        builder.scheme("http");
        switch (findBy) {
            case FIND_BY_POPULAR:
                builder.appendPath(POPULAR);
                break;
            case FIND_BY_TOP_RATED:
                builder.appendPath(TOP_RATED);
                break;
            default:
                return null;
        }
        builder.appendQueryParameter(API_KEY, KEY_VALUE);
        URL moviesURL = null;
        try {
            moviesURL = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return moviesURL;
    }


    public static URL getMovieDetailsURL(int get, int movieId) {
        Uri.Builder builder = Uri.parse(MOVIE_DB_URL).buildUpon();
        builder.scheme("http");
        builder.appendPath("" + movieId);
        switch (get) {
            case GET_TRAILER_KEY:
                builder.appendPath(TRAILERS);
                break;
            case GET_REVIEWS:
                builder.appendPath(REVIEWS);
                break;
            case GET_DETAILS:
                break;
            default:
                return null;
        }
        builder.appendQueryParameter(API_KEY, KEY_VALUE);
        URL moviesURL = null;
        try {
            moviesURL = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return moviesURL;
    }


    public static String getJSONResponse(URL moviesURL) throws IOException {

        if (moviesURL == null) {
            return null;
        }

        HttpURLConnection httpURLConnection = (HttpURLConnection) moviesURL.openConnection();
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.connect();
        String JSONResponse = null;
        try {
            InputStream stream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                JSONResponse = scanner.next();
            }
        } finally {
            httpURLConnection.disconnect();
        }
        return JSONResponse;
    }
}
