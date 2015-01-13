package com.eip.fastordering;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class ItemStruct {

    /***
     * Attributes
     */
    private String _mId;
    private String _mComment;
    private String _mCooking;

    /***
     * Methods
     */

    ItemStruct(JSONObject item) {
        try {
            _mId = item.getString("id");
            _mComment = item.getString("comment");
            _mCooking = item.getString("cooking");
        } catch (JSONException e) {

        }
        Log.d("ITEM DETAILED ORDER", "" + _mId + " " + _mComment + " " + _mCooking);
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mComment() {
        return _mComment;
    }

    public String get_mCooking() {
        return _mCooking;
    }
}
