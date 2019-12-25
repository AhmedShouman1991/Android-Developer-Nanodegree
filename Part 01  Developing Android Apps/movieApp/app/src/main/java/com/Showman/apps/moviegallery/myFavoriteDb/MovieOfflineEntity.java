package com.Showman.apps.moviegallery.myFavoriteDb;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "myFavoriteMovies", indices = {@Index(value = {"movieId"}, unique = true)} )
public class MovieOfflineEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int movieId;
    private String title;
    private String posterFileName;
    private String genres;
    private double rating;

    public MovieOfflineEntity(int id, int movieId, String title, String genres, double rating) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
    }
    @Ignore
    public MovieOfflineEntity(int movieId, String title, String genres, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterFileName() {
        return posterFileName;
    }

    public void setPosterFileName(String posterFileName) {
        this.posterFileName = posterFileName;
    }

    public String getGenres() {
        return genres;
    }

    public double getRating() {
        return rating;
    }
}
