package com.eip.fastordering.struct;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class CategoryStruct {

	private String _mCategoryName;
	private String _mId;
	private ArrayList<String> _mIds = new ArrayList<String>();

	/**
	 * Constructor
	 * @param cat
	 */
	public CategoryStruct(JSONObject cat) {
		try {
			_mCategoryName = cat.getString("name");
			_mId = cat.getString("id");
		} catch (JSONException e) {
			Log.d("CATEGORYSTRUCT", "EXCEPTION JSON:" + e.toString());
		}
	}

	public CategoryStruct(String cat, JSONObject cats) {
		JSONArray arr;
		try {
			JSONArray arrCats = cats.getJSONArray("elements");
			for (int j = 0; j < arrCats.length(); ++j) {
				if (cat.equals(arrCats.getJSONObject(j).getString("id"))) {
					_mCategoryName = arrCats.getJSONObject(j).getString("name");
					arr = arrCats.getJSONObject(j).getJSONArray("ids");
					for (int i = 0; i < arr.length(); ++i) {
						_mIds.add(arr.getString(i));
					}
				}
			}
		} catch (JSONException e) {
			Log.d("CATEGORYSTRUCT", "EXCEPTION JSON:" + e.toString());
		}
	}

	public String get_mCategoryName() {
		return _mCategoryName;
	}

	public ArrayList<String> get_mIds() {
		return _mIds;
	}

	public String get_mId() {
		return _mId;
	}
}
