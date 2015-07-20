package com.eip.fastordering.struct;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ElementStruct {

	private String _mId;
	private double _mPrice;
	private String _mName;
	private List<String> _mIdsCat     = new ArrayList<String>();
	private List<String> _mIdsOptions = new ArrayList<>();

	/**
	 * Constructor
	 * @param elem
	 */
	public ElementStruct(JSONObject elem) {
		JSONArray jsonArr;
		try {
			_mId = elem.getString("id");
			_mPrice = elem.getDouble("price");
			_mName = elem.getString("name");
			jsonArr = elem.getJSONArray("categories_ids");
			for (int i = 0; i < jsonArr.length(); ++i) {
				_mIdsCat.add(jsonArr.getString(i));
			}
			//TODO Uncomment once done - Alexis
//			jsonArr = elem.getJSONArray("options");
//			for (int i = 0; i < jsonArr.length(); i++) {
//				_mIdsOptions.add(jsonArr.getString(i));
//			}

		} catch (JSONException e) {
			Log.d("ELEMENTSTRUCT", "EXCEPTION JSON:" + e.toString());
			Log.d("ELEMENTSTRUCT", "ERROR FROM:" + elem.toString());
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

	public List<String> get_mIdsCat() {
		return _mIdsCat;
	}

	public List<String> get_mIdsOptions() {
		return _mIdsOptions;
	}
}
