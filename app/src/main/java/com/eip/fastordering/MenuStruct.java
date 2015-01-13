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
    private ArrayList<CompositionStruct> _mCat = new ArrayList<CompositionStruct>();

    /***
     * Methods
     */
    MenuStruct(JSONObject menu) {
        JSONArray arr;
        try {
            _mId = menu.getString("id");
            _mName = menu.getString("name");
            arr = menu.getJSONArray("compositions");
            for (int i = 0; i < arr.length(); ++i) {
                _mCat.add(new CompositionStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
    }
}
