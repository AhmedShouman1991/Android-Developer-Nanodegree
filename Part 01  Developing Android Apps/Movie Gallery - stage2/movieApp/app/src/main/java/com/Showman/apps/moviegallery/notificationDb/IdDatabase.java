package com.Showman.apps.moviegallery.notificationDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.Showman.apps.moviegallery.myFavoriteDb.MovieDAO;
import com.Showman.apps.moviegallery.myFavoriteDb.MovieOfflineEntity;

@Database(entities = {IdEntry.class}, version = 1, exportSchema = false)
public abstract class IdDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Ids";
    private static IdDatabase sInstance;

    public static IdDatabase getInstance(Context context) {

        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        IdDatabase.class, DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract IdDAO idDAO();

}
