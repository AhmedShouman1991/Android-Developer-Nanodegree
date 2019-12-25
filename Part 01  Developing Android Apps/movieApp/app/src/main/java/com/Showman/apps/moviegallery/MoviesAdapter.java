package com.Showman.apps.moviegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Showman.apps.moviegallery.model.Movie;
import com.Showman.apps.moviegallery.myFavoriteDb.FavoritesDataBase;
import com.Showman.apps.moviegallery.myFavoriteDb.MovieOfflineEntity;
import com.Showman.apps.moviegallery.utils.SetupSharedPreference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private Context mContext;
    private List<Movie> moviesList;
    private OnGridItemClickListener onGridItemClickListener;
    private FavoritesDataBase fDb;


    public interface OnGridItemClickListener {
        void onItemClick(Movie movie);
    }

    public MoviesAdapter(Context context , OnGridItemClickListener onGridItemClickListener) {
        this.mContext = context;
        this.onGridItemClickListener = onGridItemClickListener;
        fDb = FavoritesDataBase.getInstance(mContext);
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_list_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder holder, int position) {
        final Movie currentMovie = moviesList.get(position);
        //Log.w("movies", currentMovie.toString());
        holder.movieTitle.setText(currentMovie.getmTitle());
        Picasso.get().load(currentMovie.getmPosterUrl().toString())
                .config(Bitmap.Config.RGB_565)
                .fit()
                .into(holder.moviePoster);
        holder.movieGenre.setText(currentMovie.getGenres());
        float rating = (float)(currentMovie.getmUserRating()) * 4/10;
        holder.movieStars.setRating(rating);
        final MovieOfflineEntity movieOfflineEntity = new MovieOfflineEntity(
                currentMovie.getmMovieId(),
                currentMovie.getmTitle(),
                currentMovie.getGenres(),
                currentMovie.getmUserRating());
        holder.setMoviePosterURL(currentMovie.getmPosterUrl().toString());
        holder.setMovieOfflineEntity(movieOfflineEntity);
    }

    @Override
    public int getItemCount() {
        if (this.moviesList == null) {
            return 0;
        }
        return moviesList.size();
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView moviePoster;
        private TextView movieTitle;
        private TextView movieGenre;
        private RatingBar movieStars;
        private LinearLayout movieInfoBox;
        private MovieOfflineEntity movieOfflineEntity;
        private String moviePosterURL;

        int i = 0;

        private void setupTheme() {
            String themeValue = SetupSharedPreference.setupTheme(mContext);

            if (themeValue.equals(mContext.getString(R.string.theme_dark_value))) {
            movieTitle.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_dark));
            movieGenre.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_dark));
            movieInfoBox.setBackgroundColor(mContext.getResources().getColor(R.color.movie_item_info_box_color_dark));

            } else if (themeValue.equals(mContext.getString(R.string.theme_light_value))) {
            movieTitle.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_light));
            movieGenre.setTextColor(mContext.getResources().getColor(R.color.movie_item_label_and_rate_color_light));
            movieInfoBox.setBackgroundColor(mContext.getResources().getColor(R.color.movie_item_info_box_color_light));
            }
        }

        void setMovieOfflineEntity(MovieOfflineEntity movieOfflineEntity) {
            this.movieOfflineEntity = movieOfflineEntity;
        }

        void setMoviePosterURL(String moviePosterURL){
            this.moviePosterURL = moviePosterURL;
        }


        void insertToDatabaseAndInternalStorage() {
            String url = this.moviePosterURL;
            try {
                Bitmap moviePoster = Picasso.get().load(url).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                moviePoster.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                moviePoster.recycle();

                String filename = System.currentTimeMillis() + ".jpg";
                FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(byteArray);
                Log.e("saving to internStorage", "_______Done______");
                movieOfflineEntity.setPosterFileName(filename);
                fDb.movieDAO().addMovie(movieOfflineEntity);

                Log.e("added to database", "_____Done_____");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.list_item_movie_img);
            movieTitle = itemView.findViewById(R.id.list_item_movie_title);
            movieGenre = itemView.findViewById(R.id.list_item_movie_genre);
            movieStars = itemView.findViewById(R.id.list_item_rating_bar);
            movieStars.setMax(10);
            movieStars.setNumStars(4);
            movieStars.setStepSize(0.1f);
            movieInfoBox = itemView.findViewById(R.id.movie_info_box);
            itemView.setOnClickListener(this);
            setupTheme();
        }

        @Override
        public void onClick(View v) {
            i++;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (i == 1) {
                        int movieIndex = getAdapterPosition();
                        Movie movie = moviesList.get(movieIndex);
                        onGridItemClickListener.onItemClick(movie);
                    } else if (i == 2){
                        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                insertToDatabaseAndInternalStorage();
                            }
                        });
                        Toast.makeText(mContext, "Saved to favorites", Toast.LENGTH_SHORT).show();
                    }
                    i = 0;
                }
            }, 500);


        }

    }
}
