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
    private String _mOptions;
    private String _mStatus;
    private int _mQty;

    /***
     * Methods
     */

    ItemStruct(JSONObject item) {
        try {
            _mId = item.getString("id");
            _mComment = item.getString("comment");
            _mOptions = item.getString("options");
            _mStatus = item.getString("status");
            _mQty = item.getInt("qty");
        } catch (JSONException e) {
            Log.d("ITEMSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mComment() {
        return _mComment;
    }

    public String get_mOptions() {
        return _mOptions;
    }

    public String get_mStatus() {
        return _mStatus;
    }

    public int get_mQty() {
        return _mQty;
    }
}
