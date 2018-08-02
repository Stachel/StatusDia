package ru.cdc.statusdia.core.loaders;

import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.model.LinkCondition;
import ru.cdc.statusdia.core.model.StatusAttr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StatusAttrLoader {

    private static final String TAG = StatusAttrLoader.class.getSimpleName();

    public static HashMap<Integer, ArrayList<StatusAttr>> loadStatusAttr() {
        Logger.info(TAG,"loading data from DS_ObjectsAttributes");

        HashMap<Integer, ArrayList<StatusAttr>> list = new HashMap<>();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:support_optimum.db3");

            ResultSet c = connection.createStatement().executeQuery("select oa.id, oa.AttrID, a.AttrName " +
                    "from DS_ObjectsAttributes oa " +
                    "left join ds_Attributes a on a.AttrID = oa.AttrID " +
                    "where oa.Attrid in (1666, 4166, 4115, 4125, 4148, 4101, 4118) AND AttrText > 0 ");

            while (c.next()) {
                int statusId = c.getInt(1);
                StatusAttr item = new StatusAttr(
                        statusId,
                        c.getInt(2),
                        c.getString(3)
                );

                if (list.containsKey(statusId) == false) {
                    list.put(statusId, new ArrayList<>());
                }
                list.get(statusId).add(item);
            }
        } catch(SQLException e) {

            // if the error message is "out of memory",
            // it probably means no database file is found
            Logger.error(TAG, "Exception during loadStatusAttr(): %s", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

                // connection close failed.
                Logger.error(TAG, "Exception during loadStatusAttr(): %s", e);
            }
        }

        return list;
    }
}