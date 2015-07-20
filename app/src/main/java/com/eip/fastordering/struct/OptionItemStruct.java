package com.eip.fastordering.struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Mewen on 20-Jul-15.
 */
public class OptionItemStruct {

	private String                  _name         = "";
	private String                  _id           = "";
	private HashMap<String, String> _optionValues = null;

	/**
	 * Constructor
	 * @param optionCat
	 */
	public OptionItemStruct(JSONObject optionCat) {
		try {
			_name = optionCat.getString("name");
			_id = optionCat.getString("id");
			_optionValues = new HashMap<>();
			JSONArray optionValues = optionCat.getJSONArray("values");
			for (int i = 0; i < optionValues.length(); i++) {
				JSONObject optionValue = optionValues.getJSONObject(i);
				_optionValues.put(optionValue.getString("id"), optionValue.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String get_name() {
		return _name;
	}

	public String get_id() {
		return _id;
	}

	public HashMap<String, String> get_optionValues() {
		return _optionValues;
	}
}
