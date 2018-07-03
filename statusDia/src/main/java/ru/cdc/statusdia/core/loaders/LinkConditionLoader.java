package ru.cdc.statusdia.core.loaders;

import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.model.LinkCondition;
import ru.cdc.statusdia.core.model.StatusLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LinkConditionLoader {

    private static final String TAG = LinkConditionLoader.class.getSimpleName();

    public static HashMap<Integer, ArrayList<LinkCondition>> loadLinkCondition() {
        Logger.info(TAG,"loading data from DS_WorkflowsActivitiesLinksConditions");

        HashMap<Integer, ArrayList<LinkCondition>> list = new HashMap<>();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:support_optimum.db3");

            ResultSet c = connection.createStatement().executeQuery("SELECT conds.LinkID, conds.TypeID, conds.ID, " +
                    "COALESCE(dt.DocTypeName, av1.AttrValueName, av2.AttrValueName) " +
                    "FROM DS_WorkflowsActivitiesLinksConditions conds " +
                    "Left join DocTypes dt on conds.ID = dt.DocTypeID " +
                    "left join DS_AttributesValues av1 on av1.AttrValueID = conds.ID and av1.attrid = 1104 " +
                    "left join DS_AttributesValues av2 on av2.AttrValueID = conds.ID and av2.attrid = 1823 " +
                    "order by conds.LinkID, conds.TypeID");

            while (c.next()) {
                int linkId = c.getInt(1);
                LinkCondition item = new LinkCondition(
                        linkId,
                        c.getInt(2),
                        c.getInt(3),
                        c.getString(4)
                );

                if (list.containsKey(linkId) == false) {
                    list.put(linkId, new ArrayList<>());
                }
                list.get(linkId).add(item);
            }
        } catch(SQLException e) {

            // if the error message is "out of memory",
            // it probably means no database file is found
            Logger.error(TAG, "Exception during loadLinkCondition(): %s", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

                // connection close failed.
                Logger.error(TAG, "Exception during loadLinkCondition(): %s", e);
            }
        }

        return list;
    }
}