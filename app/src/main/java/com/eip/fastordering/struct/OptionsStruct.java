package com.eip.fastordering.struct;

import com.eip.fastordering.customs.StockMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mewen on 20-Jul-15.
 */
public class OptionsStruct {

    private Map<String, OptionItemStruct> _listOptions;
    private static OptionsStruct _instance = null;

    /**
     * Constructor
     *
     * @param optionsJson
     */
    private OptionsStruct(JSONObject optionsJson) {
        JSONArray optionsArray;
        try {
            _listOptions = new HashMap<>();
            optionsArray = optionsJson.getJSONArray("elements");
            for (int i = 0; i < optionsArray.length(); i++) {
                JSONObject optionCat = optionsArray.getJSONObject(i);
                _listOptions.put(optionCat.getString("id"), new OptionItemStruct(optionCat));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OptionsStruct getInstance() {
        if (_instance == null)
            try {
                _instance = new OptionsStruct(new JSONObject(StockMenu.instance().read("/options")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return  _instance;
    }

    /**
     * Get the set of options for a specified id
     *
     * @param id
     * @return
     */
    public OptionItemStruct getOptionsById(String id) {
        return _listOptions.get(id);
    }

    /**
     * Return the name of category of options
     *
     * @param id
     * @return
     */
    public String getNameGroupOptionById(String id) {
        if (_listOptions.get(id) != null) {
            return _listOptions.get(id).get_name();
        }
        return null;
    }

    /**
     * Return the name of a specific option
     *
     * @param id
     * @return
     */
    public String getNameOptionById(String id) {
        for (OptionItemStruct options : _listOptions.values()) {
            if (options.get_optionValues().containsKey(id))
                return options.get_optionValues().get(id);
        }
        return null;
    }
}
