package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import com.udacity.sandwichclub.utils.RecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Sandwich> sandwichesList  = new ArrayList<>();
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        for (String json : sandwiches) {
            Sandwich sandwich = JsonUtils.parseSandwichJson(json);
            sandwichesList.add(sandwich);
        }
       RecyclerAdapter adapter = new RecyclerAdapter(this);
        adapter.setSandwichList(sandwichesList);
        // Simplification: Using a ListView instead of a RecyclerView
        RecyclerView recyclerView = findViewById(R.id.sandwiches_recView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int itemPosition) {
        launchDetailActivity(itemPosition);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }
}
