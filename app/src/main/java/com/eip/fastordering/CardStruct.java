package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CardStruct {

    /***
     * Attributes
     */

    private ArrayList<CategoryStruct> _mCategories = new ArrayList<CategoryStruct>();
    private String _mId;

    /***
     * Methods
     */

    CardStruct(JSONObject card, JSONObject cats) {
        JSONObject alacarte;
        String idCompo;
        try {
            Log.d("ALACARTE", "1");
            alacarte = card.getJSONObject("alacarte");
            Log.d("ALACARTE", "2");
            _mId = alacarte.getString("id");
            Log.d("ALACARTE", "3");

            JSONArray arrCats = alacarte.getJSONArray("cats");
            Log.d("ALACARTE", "4");
            for (int i = 0; i < arrCats.length(); ++i) {
                _mCategories.add(new CategoryStruct(arrCats.getString(i), cats));
            }
        } catch (JSONException e) {
            Log.d("ALACARTE", "ERRRRRRRRORRRR");
        }
    }

    public ArrayList<CategoryStruct> get_mCategories() {
        return _mCategories;
    }

    public String get_mId() {
        return _mId;
    }
}
