package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class CategoryStruct {

    /***
     * Attributes
     */

    private String _mCategoryName;
    private ArrayList<String> _mIds = new ArrayList<String>();

    /***
     * Methods
     */

    CategoryStruct(JSONObject cat) {
        JSONArray arr;
        try {
            _mCategoryName = cat.getString("name");
            arr = cat.getJSONArray("ids");
            for (int i = 0; i < arr.length(); ++i) {
                _mIds.add(arr.getString(i));
            }
        } catch (JSONException e) {

        }
    }
}
