package ru.cdc.statusdia.core.model;

public class StatusAttr {

    private int _statusId;
    private int _attrId;
    private String _name;

    public StatusAttr (int statusId, int attrId, String name) {
        _statusId = statusId;
        _attrId = attrId;
        _name = name;
    }

    public int getStatusID() {
        return _statusId;
    }

    public String getValue() {
        return String.format("<li>%s (AttrID=%d)</li>", _name, _attrId);
    }

}
