package ru.cdc.statusdia.core.loaders;

import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.model.SecondStatus;
import ru.cdc.statusdia.core.model.StatusAttr;

import javax.net.ssl.SSLContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SecondStatusLoader {

    private static final String TAG = SecondStatusLoader.class.getSimpleName();

    public static HashMap<Integer, SecondStatus> loadSecondStatus() {
        Logger.info(TAG,"loading data from DS_Forest");

        HashMap<Integer, SecondStatus> list = new HashMap<>();
        ArrayList<SecondStatus> all = new ArrayList<>();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:support_optimum.db3");

            ResultSet c = connection.createStatement().executeQuery("select f.ID, f.GUID, f.Father, f.DictId, " +
                    "coalesce(av.AttrValueName, dt.DocTypeName, '-'), ifnull(av.AttrID, '') " +
                    "from DS_Forest f " +
                    "left join ds_AttributesValues av on av.AttrValueID = f.ID and av.AttrID in (1104, 1653) " +
                    "left join DocTypes dt on dt.DocTypeID = f.ID " +
                    "where treeid = 233 " +
                    "order by f.Dept ");

            while (c.next()) {
                int id = c.getInt(1);
                int guid = c.getInt(2);
                int father = c.getInt(3);
                int dictId = c.getInt(4);
                String name = c.getString(5);

                SecondStatus item = new SecondStatus(id, guid, name);

                if (dictId == 9) {
                    list.put(id, item);
                }

                all.add(item);

                for (SecondStatus status : all) {
                    if (status.guid() == father) {
                        status.addChild(item);
                    }
                }

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


        // Remove empty statuses
        /*for (SecondStatus type : list.values()) {
            Iterator<SecondStatus> iterator = type.childs().iterator();
            while (iterator.hasNext() == true) {
                SecondStatus status = iterator.next();
                if (status.childs().isEmpty()) {
                    iterator.remove();
                }
            }
        }*/


        return list;
    }
}
