package com.eip.fastordering.struct;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class OrderStruct {

    private String _mNumOrder;
    private String _mNumTable;
    private String _mNumPA;
    private String _mHour;
    private String _mDate;

    /**
     * Constructor
     *
     * @param cmd
     */
    public OrderStruct(JSONObject cmd) {
        super();
        Log.d("ORDERSTRUCT", "ORDER=" + cmd.toString());
        try {
            this._mNumOrder = cmd.getString("numOrder");
            this._mNumTable = cmd.getString("numTable");
            this._mNumPA = cmd.getString("numPA");
            this._mHour = cmd.getString("hour");
            this._mDate = cmd.getString("date");
        } catch (JSONException e) {
            Log.d("ORDERSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public String get_mNumOrder() {
        return _mNumOrder;
    }

    public String get_mNumTable() {
        return _mNumTable;
    }

    public String get_mNumPA() {
        return _mNumPA;
    }

    public String get_mHour() {
        return _mHour;
    }

    public String get_mDate() {
        return _mDate;
    }
}
