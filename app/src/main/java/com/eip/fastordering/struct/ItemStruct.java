package com.eip.fastordering.struct;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mewen on 13-Jan-15.
 */
public class ItemStruct {

	private String _mId;
	private String _mComment;
	private Map<String, String> _mOptions;
	private String _mStatus;
	private int    _mQty;

	/**
	 * Constructor
	 * @param item
	 */
	public ItemStruct(JSONObject item) {
		_mOptions = new HashMap<>();
		try {
			_mId = item.getString("id");
			_mComment = item.getString("comment");

//			_mOptions = item.getString("options");
			_mStatus = item.getString("status");
			_mQty = item.getInt("qty");
			JSONArray opt = item.getJSONArray("options");
			for (int i = 0; i < opt.length(); i++) {
				_mOptions.put(opt.getJSONObject(i).getString("id"), opt.getJSONObject(i).getString("qty"));
			}
		} catch (JSONException e) {
			Log.d("ITEMSTRUCT", "EXCEPTION JSON:" + e.toString());
		}
	}

	public String get_mId() {
		return _mId;
	}

	public String get_mComment() {
		return _mComment;
	}

	public Map<String, String> get_mOptions() {
		return _mOptions;
	}

	public String get_mStatus() {
		return _mStatus;
	}

	public int get_mQty() {
		return _mQty;
	}
}
