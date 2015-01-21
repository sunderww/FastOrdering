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

    private String _mNameCompo;
    private int _mPrice;
    private ArrayList<CategoryStruct> _mCat = new ArrayList<CategoryStruct>();

    /***
     * Methods
     */

    CompositionStruct(JSONObject cat) {
        JSONArray arr;
        try {
            _mPrice = cat.getInt("price");
            _mNameCompo = cat.getString("name");
            arr = cat.getJSONArray("cat");
            for (int i = 0; i < arr.length(); ++i) {
                _mCat.add(new CategoryStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
    }

    public int get_mPrice() {
        return _mPrice;
    }

    public ArrayList<CategoryStruct> get_mCat() {
        return _mCat;
    }

    public String get_mNameCompo() {
        return _mNameCompo;
    }
}
