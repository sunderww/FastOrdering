package com.eip.fastordering.customs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MewSoul on 10/09/2015.
 */
public class StockMenu {

    private static Context context;
    private SharedPreferences sharedPreferences;
    private static StockMenu instance;

    private StockMenu() {
    }

    public static StockMenu instance() {
        if (instance == null) instance = new StockMenu();
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public void write(String key, String value) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, value).commit();
    }

    public String read(String key) {
       return sharedPreferences.getString(key, "toto");
    }
}
