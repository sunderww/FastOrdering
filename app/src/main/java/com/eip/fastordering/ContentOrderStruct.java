package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class ContentOrderStruct {

    /***
     * Attributes
     */

    private String _mId;
    private ArrayList<ItemStruct> _mItems = new ArrayList<ItemStruct>();

    /***
     * Methods
     */

    ContentOrderStruct(JSONObject menu) {
        JSONArray arr;
        try {
            _mId = menu.getString("menuId");
            arr = menu.getJSONArray("content");
            for (int i = 0; i < arr.length(); ++i) {
                _mItems.add(new ItemStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d("CONTENTORDERSTRUCT", "EXCEPTION JSON");
        }
    }

    public String get_mId() {
        return _mId;
    }

    public ArrayList<ItemStruct> get_mItems() {
        return _mItems;
    }
}
