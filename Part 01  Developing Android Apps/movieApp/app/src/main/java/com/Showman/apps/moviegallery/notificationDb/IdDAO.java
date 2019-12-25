package com.Showman.apps.moviegallery.notificationDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IdDAO {

    @Query("SELECT * FROM idTable")
    List<IdEntry> getAll();

    @Query("DELETE FROM idTable")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<IdEntry> ids);

}
