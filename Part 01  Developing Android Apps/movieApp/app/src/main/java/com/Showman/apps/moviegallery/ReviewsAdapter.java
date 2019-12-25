package com.Showman.apps.moviegallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Showman.apps.moviegallery.model.Reviews;
import com.firebase.jobdispatcher.Constraint;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Reviews> reviews;
    private Context mContext;

    public ReviewsAdapter(Context mContext) {
    this.mContext = mContext;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {
        Reviews review = reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == this.reviews) return 0;
        return  this.reviews.size();
    }


    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView content;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_content);
        }
    }

}
