package com.eip.fastordering.struct;

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
    private String _mId;
    private String _mMenuId;
    private ArrayList<CategoryStruct> _mCat = new ArrayList<CategoryStruct>();

    /***
     * Methods
     */

    public CompositionStruct(JSONObject compo, ArrayList<CategoryStruct> cats) {
        try {
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
            } catch (JSONException e) {
                Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
                Log.d("COMPOSITION STRUCT", "ERROR FROM:" + compo.toString());
            }

        } catch (JSONException e) {
            Log.d("COMPOSITION STRUCT", "EXCEPTION JSON:" + e.toString());
        }
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
