package com.eip.fastordering;

/**
 * Created by Mewen on 28-Dec-14.
 */
public class OrderStruct {

    private String numOrder;
    private String numTable;
    private String numPA;
    private String hour;
    private String date;

    private String title;
    private String description;

    public OrderStruct(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
