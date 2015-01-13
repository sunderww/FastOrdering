package com.eip.fastordering;


public class NotifStruct {

    /***
     * Attributes
     */

    private String _mTitle;
    private String _mDescription;

    /***
     * Methods
     */

    public NotifStruct(String title, String description) {
        super();
        this._mTitle = title;
        this._mDescription = description;
    }

    public String getTitle() {
        return _mTitle;
    }

    public String getDescription() {
        return _mDescription;
    }
}
