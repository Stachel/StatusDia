package ru.cdc.statusdia.core.model;

import ru.cdc.statusdia.core.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class StatusLink {

    private int _linkID;
    private int _dictID1;
    private int _objectTypeID1;
    private String _docType1;
    private int _statusID1;
    private String _status1;
    private int _recordID1;

    private int _dictID2;
    private int _objectTypeID2;
    private String _docType2;
    private int _statusID2;
    private String _status2;
    private int _recordID2;


    public StatusLink(int linkID, int dictID1, int objectTypeID1, String docType1, int statusID1, String status1, int recordID1,
                      int dictID2, int objectTypeID2, String docType2, int statusID2, String status2, int recordID2) {
        _linkID = linkID;
        _dictID1 = dictID1;
        _objectTypeID1 = objectTypeID1;
        _docType1 = docType1;
        _statusID1 = statusID1;
        _status1 = status1;
        _recordID1 = recordID1;
        _dictID2 = dictID2;
        _objectTypeID2 = objectTypeID2;
        _docType2 = docType2;
        _statusID2 = statusID2;
        _status2 = status2;
        _recordID2 = recordID2;
    }

    public int getLinkID() {
        return _linkID;
    }

    public String getType1() {
        if (_dictID1 == 20) {
            return Utils.encode("Маршрут");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(_docType1);
        sb.append(" (DocTypeID=");
        sb.append(_objectTypeID1);
        sb.append(")");
        return sb.toString();
    }

    public String getStatus1() {
        return _status1;
    }

    public int getStatusID1() {
        return _statusID1;
    }

    public int getRecordID1() {
        return _recordID1;
    }

    public String getType2() {
        if (_dictID2 == 20) {
            return Utils.encode("Маршрут");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(_docType2);
        sb.append(" (DocTypeID=");
        sb.append(_objectTypeID2);
        sb.append(")");
        return sb.toString();
    }

    public String getStatus2() {
        return _status2;
    }

    public int getStatusID2() {
        return _statusID2;
    }

    public int getRecordID2() {
        return _recordID2;
    }

    public boolean forNode(StatusNode child) {
        return _recordID1 == child.getGuid() && _statusID1 == child.getId();
    }
}
