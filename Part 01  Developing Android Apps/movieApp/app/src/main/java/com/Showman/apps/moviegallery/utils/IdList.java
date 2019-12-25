package com.Showman.apps.moviegallery.utils;

import com.Showman.apps.moviegallery.model.Movie;
import com.Showman.apps.moviegallery.notificationDb.IdEntry;

import java.util.ArrayList;
import java.util.List;

public class IdList {

    public static long getListCode(List<IdEntry>idList) {
      long code = 0;
      for (IdEntry id : idList) {
          code += id.getId();
      }
      return code;
    }


    public synchronized static List<IdEntry> transformToIds(List<Movie> data) {
        List<IdEntry> ids = new ArrayList<>();
        for (Movie movie : data) {
            int id = movie.getmMovieId();
            ids.add(new IdEntry(id));
        }
        return ids;
    }
}
