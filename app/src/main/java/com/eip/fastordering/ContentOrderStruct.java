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
    private String _mGlobalComment;
    private ArrayList<ItemStruct> _mItems = new ArrayList<ItemStruct>();

    /***
     * Methods
     */

    ContentOrderStruct(JSONObject menu) {
        JSONArray arr;
        try {
            _mId = menu.getString("menu_id");
            _mGlobalComment = menu.getString("global_comment");
            arr = menu.getJSONArray("content");
            for (int i = 0; i < arr.length(); ++i) {
                _mItems.add(new ItemStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
        Log.d("MENU DETAILED", "" + _mId + " " + _mGlobalComment + " " + _mItems.toString());
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mGlobalComment() {
        return _mGlobalComment;
    }

    public ArrayList<ItemStruct> get_mItems() {
        return _mItems;
    }
}
