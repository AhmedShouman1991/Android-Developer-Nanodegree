package com.udacity.sandwichclub.utils;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<Sandwich> mSandwichList;

    private OnListItemClickListener onListItemClickListener;

    public interface OnListItemClickListener {
        void onItemClick(int itemPosition);
    }

    public RecyclerAdapter(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setSandwichList(List<Sandwich> sandwichList) {
        this.mSandwichList = sandwichList;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        Sandwich sandwich = mSandwichList.get(position);
        holder.sandwichName.setText(sandwich.getMainName());
        holder.sandwichDescription.setText(sandwich.getDescription());
        Picasso.with(holder.sandwichImg.getContext()).load(sandwich.getImage()).into(holder.sandwichImg);
    }

    @Override
    public int getItemCount() {
        if (this.mSandwichList == null || this.mSandwichList.size() == 0) {
            return 0;
        }
        return this.mSandwichList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sandwichName;
        TextView sandwichDescription;
        ImageView sandwichImg;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            this.sandwichName = itemView.findViewById(R.id.sandwich_name);
            this.sandwichDescription = itemView.findViewById(R.id.sandwich_description);
            this.sandwichImg = itemView.findViewById(R.id.sandwich_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            onListItemClickListener.onItemClick(itemPosition);
        }
    }
}