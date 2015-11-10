package com.eip.fastordering.struct;

import com.eip.fastordering.fragment.OrderFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MewSoul on 07/10/2015.
 */
public class DataDishStruct {

    private String mComment;
    private Map<String, Map<String, String>> mOptions;

    public DataDishStruct(ElementStruct elementStruct) {
        mComment = "";
        mOptions = new HashMap<>();

        //TODO Fetch options correctly from dish and put them here.
        if (!elementStruct.get_mIdsOptions().isEmpty())
            System.out.println("Element has options");
        else
            System.out.println("Element has no options");


        for (String string : elementStruct.get_mIdsOptions()) {
            System.out.println("Current cat option is=" + OrderFragment.get_mOptions().getNameGroupOptionById(string));
            Map<String, String> detailedOptions = new HashMap<>();
            for (String string1 : OrderFragment.get_mOptions().getOptionsById(string).get_optionValues().keySet()) {
                detailedOptions.put(string1, "0");
                System.out.println("Setting option = " + OrderFragment.get_mOptions().getNameOptionById(string1));
            }
            mOptions.put(string, detailedOptions);
        }
    }

    public DataDishStruct() {
        mComment = "";
        mOptions = new HashMap<>();
    }

    public void addCategoryOption(String idCategory) {
        mOptions.put(idCategory, new HashMap<String, String>());
    }

    public void addOptionToCategory(String idCategory, String idOption, String value) {
        mOptions.get(idCategory).put(idOption, value);
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public Map<String, Map<String, String>> getmOptions() {
        return mOptions;
    }
}
