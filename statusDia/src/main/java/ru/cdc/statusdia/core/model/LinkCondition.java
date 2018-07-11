package ru.cdc.statusdia.core.model;

import ru.cdc.statusdia.core.Utils;

public class LinkCondition {

    private int _linkId;
    private int _typeId;
    private int _id;
    private String _desc;

    public LinkCondition (int linkId, int typeId, int id, String desc) {
        _linkId = linkId;
        _typeId = typeId;
        _id = id;
        _desc = desc;
    }

    public int getTypeID() {
        return _typeId;
    }

    public String getType() {
        return String.format("%s (typeID=%s)", getTypeName(), _typeId);
    }

    private String getTypeName() {
        switch (_typeId) {
            case 2830013:
                return Utils.encode("Тип документа");
            case 40000093:
                return Utils.encode("Статус документа");
            case 40000943:
                return Utils.encode("Группировка");
        }
        return "?";
    }

    public String getValue() {
        return String.format("%s (ID=%s)", _desc, _id);
    }
}
