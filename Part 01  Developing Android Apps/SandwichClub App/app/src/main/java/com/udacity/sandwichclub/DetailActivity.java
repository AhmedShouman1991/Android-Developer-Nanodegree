package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private TextView mAlsoKnownAsTextView;
    private TextView mOriginTextView;
    private TextView mIngredientsTextView;
    private TextView mDescriptionTextView;
    private TextView mSandwichNAme;
    private Sandwich currantSandwich;
    private TextView mAlsoKnownAsLabel;
    private TextView mOriginLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        android.support.v7.widget.Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        //ActionBar actionBar = getActionBar();
//        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mAlsoKnownAsLabel = findViewById(R.id.also_known_as_label);
        mOriginLabel= findViewById(R.id.place_of_origin_label);
        mSandwichNAme = findViewById(R.id.main_name_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        this.currantSandwich = sandwich;
        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        List<String> alsoKnownList = this.currantSandwich.getAlsoKnownAs();
        StringBuilder builder = new StringBuilder();
        for (String known : alsoKnownList) {
            builder.append(known);
            builder.append(", ");
        }
        String alsoKnown = builder.toString();
        if (alsoKnown.length() > 0) {
            mAlsoKnownAsTextView.setText(alsoKnown.substring(0, alsoKnown.length() - 2));
        } else {
            mAlsoKnownAsLabel.setVisibility(View.GONE);
            mAlsoKnownAsTextView.setVisibility(View.GONE);
        }
        String origin = this.currantSandwich.getPlaceOfOrigin();
        if (origin.length() > 0) {
            mOriginTextView.setText(origin);
        } else {
            mOriginLabel.setVisibility(View.GONE);
            mOriginTextView.setVisibility(View.GONE);
        }

        List<String> ingredientsList = this.currantSandwich.getIngredients();
        StringBuilder builder2 = new StringBuilder();
        for (String ing : ingredientsList) {
            builder2.append("- ");
            builder2.append(ing);
            builder2.append("\n");
        }
        String ingredientsListAsString = builder2.toString();
        mIngredientsTextView.setText(ingredientsListAsString.substring(0, ingredientsListAsString.length() - 1));

        mDescriptionTextView.setText(this.currantSandwich.getDescription());

        mSandwichNAme.setText(this.currantSandwich.getMainName());
    }
}
