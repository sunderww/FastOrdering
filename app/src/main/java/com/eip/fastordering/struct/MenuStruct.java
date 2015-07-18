package com.eip.fastordering.struct;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuStruct {

    /***
     * Attributes
     */

    private String _mId;
    private String _mName;
    private ArrayList<CompositionStruct> _mCats = new ArrayList<CompositionStruct>();

    /***
     * Methods
     */

    public MenuStruct(JSONObject menu) {
        try {
            _mId = menu.getString("id");
            _mName = menu.getString("name");
        } catch (JSONException e) {

        }
    }

    public String get_mId() {
        return _mId;
    }

    public String get_mName() {
        return _mName;
    }

    public ArrayList<CompositionStruct> get_mCat() {
        return _mCats;
    }
}
