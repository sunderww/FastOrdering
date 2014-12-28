package com.eip.fastordering;

/**
 * Created by Mewen on 28-Dec-14.
 */
public class Item {
    private String title;
    private String description;

    public Item(String title, String description) {
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
