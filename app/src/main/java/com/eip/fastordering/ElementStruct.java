package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


public class ElementStruct {

    /***
     * Attributes
     */

    String _mId;
    double _mPrice;
    String _mName;
    ArrayList<String> _mIdsCat = new ArrayList<String>();

    /***
     * Methods
     */

    ElementStruct(JSONObject elem) {
        JSONArray jsonIdCat;
        try {
            _mId = elem.getString("id");
            _mPrice = elem.getDouble("price");
            _mName = elem.getString("name");
            jsonIdCat = elem.getJSONArray("categories_ids");
            for (int i = 0; i < jsonIdCat.length(); ++i) {
                _mIdsCat.add(jsonIdCat.getString(i));
            }
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

    public ArrayList<String> get_mIdsCat() {
        return _mIdsCat;
    }
}
