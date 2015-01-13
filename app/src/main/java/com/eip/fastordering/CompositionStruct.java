package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class CompositionStruct {

    /***
     * Attributes
     */

    private int _mPrice;
    private ArrayList<CategoryStruct> _mCat = new ArrayList<CategoryStruct>();

    /***
     * Methods
     */

    CompositionStruct(JSONObject cat) {
        JSONArray arr;
        try {
            _mPrice = cat.getInt("price");
            arr = cat.getJSONArray("cat");
            for (int i = 0; i < arr.length(); ++i) {
                _mCat.add(new CategoryStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
    }
}
