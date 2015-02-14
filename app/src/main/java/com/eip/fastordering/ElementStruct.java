package com.eip.fastordering;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class ElementStruct {

    /***
     * Attributes
     */

    String _mId;
    double _mPrice;
    String _mName;

    /***
     * Methods
     */

    ElementStruct(JSONObject elem) {
        try {
            _mId = elem.getString("id");
            _mPrice = elem.getDouble("price");
            _mName = elem.getString("name");
        } catch (JSONException e) {
            Log.d("ELEMENTSTRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public String get_mId() {
        return _mId;
    }

    public double get_mPrice() {
        return _mPrice;
    }

    public String get_mName() {
        return _mName;
    }

}
