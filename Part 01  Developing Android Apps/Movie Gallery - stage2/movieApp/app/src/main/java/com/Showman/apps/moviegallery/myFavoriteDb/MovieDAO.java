package com.Showman.apps.moviegallery.myFavoriteDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM myFavoriteMovies")
    LiveData<List<MovieOfflineEntity>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(MovieOfflineEntity favoriteMovie);


    @Delete
    void deleteMovie(MovieOfflineEntity movieOfflineEntity);
}
