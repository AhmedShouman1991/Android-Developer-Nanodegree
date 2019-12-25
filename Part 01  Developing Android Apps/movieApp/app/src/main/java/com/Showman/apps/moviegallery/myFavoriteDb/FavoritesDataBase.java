package com.Showman.apps.moviegallery.myFavoriteDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieOfflineEntity.class}, version = 2, exportSchema = false)
public abstract class FavoritesDataBase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "myFavorites";
    private static FavoritesDataBase sInstance;

    public static FavoritesDataBase getInstance(Context context) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoritesDataBase.class, DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract MovieDAO movieDAO();

}
