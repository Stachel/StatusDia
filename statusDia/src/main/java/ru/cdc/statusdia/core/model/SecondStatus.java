package ru.cdc.statusdia.core.model;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;

public class SecondStatus {

    private int _id;
    private int _guid;
    private String _name;

    ArrayList<SecondStatus> _childs;

    public SecondStatus (int id, int guid, String name) {
        _id = id;
        _guid = guid;
        _name = name;
        _childs = new ArrayList<>();
    }

    public void addChild(SecondStatus child) {
        _childs.add(child);
    }

    public int id() {
        return _id;
    }

    public int guid() {
        return _guid;
    }

    public String name() {
        return _name;
    }

    public ArrayList<SecondStatus>  childs() {
        return _childs;
    }
}
