package com.udacity.sandwichclub.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Sandwich> {
    public ListAdapter(@NonNull Context context, @NonNull List<Sandwich> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, parent, false);
        }
        Sandwich currantSandwich = getItem(position);
        TextView textView = convertView.findViewById(R.id.sandwich_name);
        textView.setText(currantSandwich.getMainName());

        textView = convertView.findViewById(R.id.sandwich_description);
        textView.setText(currantSandwich.getDescription());

        ImageView img = convertView.findViewById(R.id.sandwich_img);
        Picasso.with(getContext())
                .load(currantSandwich.getImage())
                .into(img);
        return convertView;
    }
}
