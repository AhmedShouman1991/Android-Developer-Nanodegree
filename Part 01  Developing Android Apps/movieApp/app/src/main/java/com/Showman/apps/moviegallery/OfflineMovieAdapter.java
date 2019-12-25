package com.Showman.apps.moviegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Showman.apps.moviegallery.myFavoriteDb.MovieOfflineEntity;
import com.Showman.apps.moviegallery.utils.SetupSharedPreference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class OfflineMovieAdapter extends RecyclerView.Adapter<OfflineMovieAdapter.OfflineHolder> {

    private Context mContext;
    private List<MovieOfflineEntity> movieOfflineList;

    public OfflineMovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<MovieOfflineEntity> getMovieOfflineList() {
        return movieOfflineList;
    }

    @NonNull
    @Override
    public OfflineMovieAdapter.OfflineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_list_item, parent, false);
        return new OfflineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfflineMovieAdapter.OfflineHolder holder, int position) {
        final MovieOfflineEntity currentMovie = movieOfflineList.get(position);
        //Log.w("movies", currentMovie.toString());
        holder.movieTitle.setText(currentMovie.getTitle());

        AppExecutors.getsInstance().getMainThread().execute(new Runnable() {
            @Override
            public void run() {
                File imageFile = new File(mContext.getFilesDir(), currentMovie.getPosterFileName());
//                String filePath = imageFile.getPath();
//                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Picasso.get().load(imageFile).fit().into(holder.moviePoster);
            }
        });
        holder.itemView.setTag(position);
        holder.movieRatingNumber.setText(currentMovie.getGenres());
        float rating = (float)(currentMovie.getRating()) * 4/10;
        holder.movieStars.setRating(rating);
    }

    @Override
    public int getItemCount() {
        if (movieOfflineList == null) return 0;
        return movieOfflineList.size();
    }

    public void setMovieOfflineList(List<MovieOfflineEntity> movieOfflineList) {
        this.movieOfflineList = movieOfflineList;
        notifyDataSetChanged();
    }

    public class OfflineHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle;
        private TextView movieRatingNumber;
        private RatingBar movieStars;
        private LinearLayout movieInfoBox;

        private void setupTheme() {
            String themeValue = SetupSharedPreference.setupTheme(mContext);

            if (themeValue.equals(mContext.getString(R.string.theme_dark_value))) {
                movieTitle.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_dark));
                movieRatingNumber.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_dark));
                movieInfoBox.setBackgroundColor(mContext.getResources().getColor(R.color.movie_item_info_box_color_dark));

            } else if (themeValue.equals(mContext.getString(R.string.theme_light_value))) {
                movieTitle.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_light));
                movieRatingNumber.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_light));
                movieInfoBox.setBackgroundColor(mContext.getResources().getColor(R.color.movie_item_info_box_color_light));
            }
        }


        OfflineHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.list_item_movie_img);
            movieTitle = itemView.findViewById(R.id.list_item_movie_title);
            movieRatingNumber = itemView.findViewById(R.id.list_item_movie_genre);
            movieStars = itemView.findViewById(R.id.list_item_rating_bar);
            movieStars.setMax(10);
            movieStars.setNumStars(4);
            movieStars.setStepSize(0.1f);
            movieInfoBox = itemView.findViewById(R.id.movie_info_box);
            setupTheme();
        }
    }
}
