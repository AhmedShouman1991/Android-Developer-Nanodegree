package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = null;
        try {
            JSONObject root = new JSONObject(json);

            JSONObject name = root.optJSONObject("name");
            String mainName = name.getString("mainName");

            JSONArray alsoKnownAsArray = name.optJSONArray("alsoKnownAs");
            ArrayList<String> alsoKnownAsList = new ArrayList<>();
            if (alsoKnownAsArray != null && alsoKnownAsArray.length() > 0 ) {
                for (int i = 0; i < alsoKnownAsArray.length(); i++) {
                    String alsoKnownAsString = alsoKnownAsArray.getString(i);
                    alsoKnownAsList.add(alsoKnownAsString);
                }
            }

            String placeOfOrigin = root.optString("placeOfOrigin");
            String description = root.optString("description");
            String imgURL = root.optString("image");

            JSONArray ingredientsArray = root.optJSONArray("ingredients");
            ArrayList<String> ingredients = new ArrayList<>();
            if (ingredientsArray != null && ingredientsArray.length() > 0) {
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    String ingredientsString = ingredientsArray.optString(i);
                    ingredients.add(ingredientsString);
                }
            }
            sandwich = new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, imgURL, ingredients);
        } catch (JSONException e) {
            Log.e("JSON", "error happened");
            e.printStackTrace();
        }
        return sandwich;
    }
}
