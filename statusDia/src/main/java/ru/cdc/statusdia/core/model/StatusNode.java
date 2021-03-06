package ru.cdc.statusdia.core.model;

public class StatusNode implements Comparable<StatusNode> {

    private int _typeId;
    private int _guid;
    private int _id;
    private String _name;

    public StatusNode(int typeId, int guid, int id, String name) {
        _typeId = typeId;
        _guid = guid;
        _id = id;
        _name = name;
    }

    @Override
    public int hashCode() {
        return _guid + (_id * 17);
    }

    @Override
    public boolean equals(Object o) {

        if (o != null && o instanceof StatusNode) {
            StatusNode node = (StatusNode)o;
            return _typeId == node._typeId && _guid == node._guid && _id == node._id;
        }

        return false;
    }

    public String getDiaHtml(int index) {
        return String.format("{ id: %d, font: { multi: true }, widthConstraint: 120, label: '<b>%s</b> \\n ID:%d GUID:%d' }, ", index, _name, _id, _guid );
    }

    public String getHtml(int index) {
        index++;
        if (index > 0) {
            return String.format("<div class=\"header\">%d. %s</div><div>ID: %s &emsp; GUID: %s</div>", index, _name, _id, _guid);
        }
        return String.format("<div class=\"header\">%s</div><div>ID: %s &emsp; GUID: %s</div>", _name, _id, _guid);
    }

    @Override
    public int compareTo(StatusNode statusNode) {
        if (statusNode._id == _id) return 0;
        if (statusNode._id < _id) return -1;
        return 1;
    }

    public int getTypeId() {
        return _typeId;
    }

    public int getGuid() {
        return _guid;
    }

    public int getId() {
        return _id;
    }
}
