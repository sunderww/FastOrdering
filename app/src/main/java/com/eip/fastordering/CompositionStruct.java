package com.eip.fastordering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class CompositionStruct {

    /***
     * Attributes
     */

    private String _mNameCompo;
    private double _mPrice;
    private String _mId;
    private String _mMenuId;
    private ArrayList<CategoryStruct> _mCat = new ArrayList<CategoryStruct>();

    /***
     * Methods
     */

    CompositionStruct(JSONObject compo, ArrayList<CategoryStruct> cats) {
        try {
            _mPrice = compo.getDouble("price");
            _mNameCompo = compo.getString("name");
            _mId = compo.getString("id");
            _mMenuId = compo.getString("menu_id");

            try {
                JSONArray arr;
                arr = compo.getJSONArray("categories_ids");
                for (int i = 0; i < arr.length(); ++i) {
                    String idCat = arr.getString(i);
                    for (CategoryStruct curCat : cats) {
                        if (curCat.get_mId().equals(idCat)) {
                            _mCat.add(curCat);
                        }
                    }
                }
                Log.d("COMPOSITION", "" + _mNameCompo );
                for (CategoryStruct item: _mCat) {
                    //Log.d("COMPOS CATS", item);
                }
            } catch (JSONException e) {
                Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
            }

        } catch (JSONException e) {
            Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    CompositionStruct(String cat, JSONArray compos, JSONObject cats) {
        JSONArray arr;
        try {
            for (int i = 0; i < compos.length(); ++i) {
                if (cat.equals(compos.getJSONObject(i).getString("id"))) {
                    _mPrice = compos.getJSONObject(i).getInt("price");
                    _mNameCompo = compos.getJSONObject(i).getString("name");
                    arr = compos.getJSONObject(i).getJSONArray("cat");
                    for (int j = 0; j < arr.length(); ++j) {
                        _mCat.add(new CategoryStruct(arr.getString(j), cats));
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public double get_mPrice() {
        return _mPrice;
    }

    public ArrayList<CategoryStruct> get_mCat() {
        return _mCat;
    }

    public String get_mNameCompo() {
        return _mNameCompo;
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mMenuId() {
        return _mMenuId;
    }
}
