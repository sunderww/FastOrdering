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

    CompositionStruct(String cat, JSONArray compos, JSONObject cats) {
        JSONArray arr;
        try {
            for (int i = 0; i < compos.length(); ++i) {
                if (cat.equals(compos.getJSONObject(i).getString("id"))) {
                    _mPrice = compos.getJSONObject(i).getInt("price");
                    _mNameCompo = compos.getJSONObject(i).getString("name");
                    arr = compos.getJSONObject(i).getJSONArray("cat");
                    for (int j = 0; j < arr.length(); ++j) {
                        _mCat.add(new CategoryStruct(arr.getString(j), cats));
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
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
