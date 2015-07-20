package com.eip.fastordering.struct;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuStruct {

	private String _mId;
	private String _mName;
	private List<CompositionStruct> _mCats = new ArrayList<CompositionStruct>();

	/**
	 * Constructor
	 * @param menu
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

	public List<CompositionStruct> get_mCat() {
		return _mCats;
	}
}
