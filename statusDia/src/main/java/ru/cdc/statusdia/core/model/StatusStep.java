package ru.cdc.statusdia.core.model;

public class StatusStep {

    private int _objectTypeId;
    private int _parentGUID;
    private int _parentID;
    private String _parentName;
    private int _childGUID;
    private int _childID;
    private String _childName;
    private int _userType;
    private int _faceType;
    private int _isPositive;
    private int _isNegative;
    private int _isNavigation;
    private int _isScan;

    private StatusNode _parent, _child;


    public StatusStep(int objectTypeId, int parentGUID, int parentID, String parentName, int childGUID, int childID, String childName,
                      int userType, int faceType, int isPositive, int isNegative, int isNavigation, int isScan) {
        _objectTypeId = objectTypeId;
        _parentGUID = parentGUID;
        _parentID = parentID;
        _parentName = parentName;
        _childGUID = childGUID;
        _childID = childID;
        _childName = childName;
        _userType = userType;
        _faceType = faceType;
        _isPositive = isPositive;
        _isNegative = isNegative;
        _isNavigation = isNavigation;
        _isScan = isScan;
    }

    public int getTypeID() {
        return _objectTypeId;
    }

    public StatusNode getParent() {
        if (_parent == null) {
            _parent = new StatusNode(_objectTypeId, _parentGUID, _parentID, _parentName);
        }
        return _parent;
    }

    public StatusNode getChild() {
        if (_child == null) {
            _child = new StatusNode(_objectTypeId, _childGUID, _childID, _childName);
        }
        return _child;
    }

    public String getHtml(int from, int to) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("{from: %d, to: %d", from, to));

        if ((_userType & 4) == 0) {
            sb.append(", dashes:true");
        }

       /* if (_isNegative > 0) {
            sb.append(", color:{color:'red', highlight:'darkred'}");
        }

        if (_isPositive > 0) {
            sb.append(", color:{color:'green', highlight:'darkgreen'}");
        }

        sb.append(", label: '");
        if (_isNavigation > 0) {
            sb.append("N");
        }
        if (_isScan > 0) {
            sb.append("S");
        }
        sb.append("'");*/

        sb.append("},\n");

        return sb.toString();
    }

    public String getStatusType() {
        if (_isPositive > 0) {
            return "positive";
        }
        if (_isNegative > 0) {
            return "negative";
        }
        return "";
    }

    public boolean isAuto() {
        return (_userType & 4) == 0;
    }

    public String getPlaceType() {
        if (_faceType == 5200001) {
            return "Визит";
        }
        if (_faceType == 5200004) {
            return "Склад";
        }
        return "";
    }

    public String getParams() {
        StringBuffer sb = new StringBuffer();
        if (_isNavigation > 0) {
            sb.append("<li>Навигатор</li>");
        }
        if (_isScan > 0) {
            sb.append("<li>Сканирование штрих-кода</li>");
        }
        return sb.toString();
    }
}
