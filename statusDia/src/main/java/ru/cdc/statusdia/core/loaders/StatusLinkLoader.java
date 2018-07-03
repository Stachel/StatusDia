package ru.cdc.statusdia.core.loaders;


import org.sqlite.SQLiteConnection;
import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.model.StatusLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatusLinkLoader {

    private static final String TAG = StatusLinkLoader.class.getSimpleName();

    public static ArrayList<StatusLink> loadStatusLink() {
        Logger.info(TAG,"loading data from DS_WorkflowsActivitiesLinks");

        ArrayList<StatusLink> list = new ArrayList();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:support_optimum.db3");

            ResultSet c = connection.createStatement().executeQuery("select links.LinkID, " +
                    "links.DictID1, links.ObjectTypeID1, dt1.DocTypeName, links.StatusID1, av1.AttrValueName, links.RecordID1, " +
                    "links.DictID2, links.ObjectTypeID2, dt2.DocTypeName, links.StatusID2, av2.AttrValueName, links.RecordID2 " +
                    "from DS_WorkflowsActivitiesLinks links " +
                    "Left join DocTypes dt1 on links.ObjectTypeID1 = dt1.DocTypeID " +
                    "Left join DocTypes dt2 on links.ObjectTypeID2 = dt2.DocTypeID " +
                    "left join DS_AttributesValues av1 on av1.AttrValueID = links.StatusID1 and av1.attrid in (1727, 1104) " +
                    "left join DS_AttributesValues av2 on av2.AttrValueID = links.StatusID2 and av2.attrid in (1727, 1104)");

            while (c.next()) {
                StatusLink item = new StatusLink(
                        c.getInt(1),
                        c.getInt(2),
                        c.getInt(3),
                        c.getString(4),
                        c.getInt(5),
                        c.getString(6),
                        c.getInt(7),
                        c.getInt(8),
                        c.getInt(9),
                        c.getString(10),
                        c.getInt(11),
                        c.getString(12),
                        c.getInt(13)
                );
                list.add(item);
            }
        } catch(SQLException e) {

            // if the error message is "out of memory",
            // it probably means no database file is found
            Logger.error(TAG, "Exception during loadStatusLink(): %s", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

                // connection close failed.
                Logger.error(TAG, "Exception during loadStatusLink(): %s", e);
            }
        }

        return list;
    }
}
