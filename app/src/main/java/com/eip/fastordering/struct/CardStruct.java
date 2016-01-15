package com.eip.fastordering.struct;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CardStruct {

	private List<CategoryStruct> _mCategories;
	private String               _mId;

	/**
	 * Constructor
	 * @param card
	 * @param cats
	 */
	public CardStruct(JSONObject card, List<CategoryStruct> cats) {
		_mCategories = new ArrayList<>();
		JSONObject alacarte;
		try {
			alacarte = card.getJSONObject("elements");
			_mId = alacarte.getString("id");
			JSONArray arrCats = alacarte.getJSONArray("compo");
			for (int i = 0; i < arrCats.length(); ++i) {
				String idCat = arrCats.getString(i);
				for (CategoryStruct curCat : cats) {
					if (curCat.get_mId().equals(idCat)) {
						_mCategories.add(curCat);
					}
				}
			}
		} catch (JSONException e) {
			Log.d("CARDSTRUCT", "EXCEPTION JSON:" + e.toString());
			e.printStackTrace();

		}
	}

	public CardStruct(JSONObject card, JSONObject cats) {
		JSONObject alacarte;
		try {
			alacarte = card.getJSONObject("elements");
			_mId = alacarte.getString("id");
			JSONArray arrCats = alacarte.getJSONArray("cats");
			for (int i = 0; i < arrCats.length(); ++i) {
				_mCategories.add(new CategoryStruct(arrCats.getString(i), cats));
			}
		} catch (JSONException e) {
			Log.d("CARDSTRUCT", "EXCEPTION JSON:" + e.toString());
		}
	}

	public List<CategoryStruct> get_mCategories() {
		return _mCategories;
	}

	public String get_mId() {
		return _mId;
	}
}
