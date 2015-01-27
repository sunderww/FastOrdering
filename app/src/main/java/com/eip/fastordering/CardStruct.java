package com.eip.fastordering;

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

    CardStruct(JSONObject card) {
        JSONObject alacarte;
        JSONArray arr;
        try {
            alacarte = card.getJSONObject("alacarte");
            _mId = alacarte.getString("id");
            arr = alacarte.getJSONArray("composition");
            for (int i = 0; i < arr.length(); ++i) {
                _mCategories.add(new CategoryStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
    }

    public ArrayList<CategoryStruct> get_mCategories() {
        return _mCategories;
    }

    public String get_mId() {
        return _mId;
    }
}
