package com.eip.fastordering;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuStruct {

    /***
     * Attributes
     */

    private String _mId;
    private String _mName;
    private ArrayList<CompositionStruct> _mCats = new ArrayList<CompositionStruct>();

    /***
     * Methods
     */

    MenuStruct(JSONObject menu) {
        try {
            _mId = menu.getString("id");
            _mName = menu.getString("name");
        } catch (JSONException e) {

        }
    }

    MenuStruct(JSONObject menu, JSONArray compos, JSONObject cats) {
        JSONArray arr;
        try {
            _mId = menu.getString("id");
            _mName = menu.getString("name");
            arr = menu.getJSONArray("compo");
            for (int i = 0; i < arr.length(); ++i) {
                _mCats.add(new CompositionStruct(arr.getString(i), compos, cats));
            }
        } catch (JSONException e) {
            Log.d("MENUSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mName() {
        return _mName;
    }

    public ArrayList<CompositionStruct> get_mCat() {
        return _mCats;
    }
}
