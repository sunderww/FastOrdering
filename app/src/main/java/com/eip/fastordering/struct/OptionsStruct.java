package com.eip.fastordering.struct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mewen on 20-Jul-15.
 */
public class OptionsStruct {

	private List<OptionItemStruct> _listOptions;

	/**
	 * Constructor
	 * @param optionsJson
	 */
	public OptionsStruct(JSONObject optionsJson) {
		JSONArray optionsArray;
		try {
			_listOptions = new ArrayList<>();
			optionsArray = optionsJson.getJSONArray("elements");
			for (int i = 0; i < optionsArray.length(); i++) {
				JSONObject optionCat = optionsArray.getJSONObject(i);
				_listOptions.add(new OptionItemStruct(optionCat));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the set of options for a specified id
	 * @param id
	 * @return
	 */
	public OptionItemStruct getOptionsById(String id) {
		for (OptionItemStruct options : _listOptions) {
			if (options.get_id().equals(id)) {
				return options;
			}
		}
		return null;
	}

	/**
	 * Return the name of category of options
	 * @param id
	 * @return
	 */
	public String getNameGroupOptionById(String id) {
		for (OptionItemStruct options : _listOptions) {
			if (options.get_id().equals(id)) {
				return options.get_name();
			}
		}
		return null;
	}

	/**
	 * Return the name of a specific option
	 * @param id
	 * @return
	 */
	public String getNameOptionById(String id) {
		for (OptionItemStruct options : _listOptions) {
			return options.get_optionValues().get(id);
		}
		return null;
	}
}
