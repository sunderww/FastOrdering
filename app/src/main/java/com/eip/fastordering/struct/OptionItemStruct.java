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
			System.out.println("Adding option cat=" + _name);
			_id = optionCat.getString("id");
			System.out.println("With id=" + _id);
			_optionValues = new HashMap<>();
			JSONArray optionValues = optionCat.getJSONArray("option");
			for (int i = 0; i < optionValues.length(); i++) {
				JSONObject optionValue = optionValues.getJSONObject(i);
				_optionValues.put(optionValue.getString("id"), optionValue.getString("name"));
				System.out.println("Option " + i + ", name=" + optionValue.getString("name") + " id=" + optionValue.getString("id"));
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
