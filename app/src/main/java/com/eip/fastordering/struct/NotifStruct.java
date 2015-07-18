package com.eip.fastordering.struct;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class NotifStruct {

    /***
     * Attributes
     */

    private String _mNumTable;
    private String _mMsg;
    private String _mDate;
    private String _mHour;

    /***
     * Methods
     */

    public NotifStruct(JSONObject notif) {
        try {
            _mNumTable = notif.getString("numTable");
            _mMsg = notif.getString("msg");
            _mDate = notif.getString("date");
            _mHour = notif.getString("hour");
        } catch (JSONException e) {
            Log.d("NOTIFSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public String get_mHour() {
        return _mHour;
    }

    public String get_mDate() {
        return _mDate;
    }

    public String get_mNumTable() {
        return _mNumTable;
    }

    public String get_mMsg() {
        return _mMsg;
    }
}
