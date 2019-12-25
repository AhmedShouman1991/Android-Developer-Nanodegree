package com.Showman.apps.moviegallery.sync;

import android.content.Context;
import android.util.Log;

import com.Showman.apps.moviegallery.utils.IdList;
import com.Showman.apps.moviegallery.model.Movie;
import com.Showman.apps.moviegallery.notificationDb.IdDatabase;
import com.Showman.apps.moviegallery.notificationDb.IdEntry;
import com.Showman.apps.moviegallery.utils.JSONParsing;
import com.Showman.apps.moviegallery.utils.NetworkUtils;
import com.Showman.apps.moviegallery.utils.NotificationUtils;
import com.Showman.apps.moviegallery.utils.SetupSharedPreference;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MoviesSyncTasks {

    synchronized public static void syncNewMovies(Context context) {
        IdDatabase idDb = IdDatabase.getInstance(context);
        URL moviesURL = NetworkUtils.getUrl(NetworkUtils.FIND_BY_POPULAR);
        String JSONMoviesCategory;
        List<Movie> moviesList = null;
        try {
            JSONMoviesCategory = NetworkUtils.getJSONResponse(moviesURL);
            moviesList = JSONParsing.getMoviesListFromJSON(JSONMoviesCategory, JSONParsing.DO_NOT_GET_DETAILS);

            if (moviesList != null && moviesList.size() > 0) {

                List<IdEntry> newIds = IdList.transformToIds(moviesList);
                long newCode = IdList.getListCode(newIds);

                List<IdEntry> existList = idDb.idDAO().getAll();
                long existCode = IdList.getListCode(existList);


                if (newCode != existCode && SetupSharedPreference.setupNotificationPreference(context)) {
                    NotificationUtils.notifyUserOfNewWeather(context);
                    idDb.idDAO().deleteAll();
                    Log.e("Services DATABASE", "DELETED");
                    List<IdEntry> ids = IdList.transformToIds(moviesList);
                    idDb.idDAO().insert(ids);
                    Log.e("Services DATABASE", "All Inserted");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
