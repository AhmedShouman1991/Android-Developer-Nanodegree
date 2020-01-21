package com.Showman.apps.moviegallery;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.Showman.apps.moviegallery.myFavoriteDb.FavoritesDataBase;
import com.Showman.apps.moviegallery.myFavoriteDb.MovieOfflineEntity;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private LiveData<List<MovieOfflineEntity>> favorites;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favorites = FavoritesDataBase.getInstance(getApplication()).movieDAO().loadAllMovies();
    }

    public LiveData<List<MovieOfflineEntity>> getFavorites() {
        return favorites;
    }
}
