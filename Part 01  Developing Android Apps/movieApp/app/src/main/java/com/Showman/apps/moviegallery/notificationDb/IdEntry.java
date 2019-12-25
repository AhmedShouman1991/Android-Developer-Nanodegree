package com.Showman.apps.moviegallery.notificationDb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "idTable")
public class IdEntry {
    @PrimaryKey(autoGenerate = false)
    private int id;

    public IdEntry(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
