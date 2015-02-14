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
        try {
            alacarte = card.getJSONObject("alacarte");
            _mId = alacarte.getString("id");
            JSONArray arrCats = alacarte.getJSONArray("cats");
            for (int i = 0; i < arrCats.length(); ++i) {
                _mCategories.add(new CategoryStruct(arrCats.getString(i), cats));
            }
        } catch (JSONException e) {
            Log.d("CARDSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public ArrayList<CategoryStruct> get_mCategories() {
        return _mCategories;
    }

    public String get_mId() {
        return _mId;
    }
}
