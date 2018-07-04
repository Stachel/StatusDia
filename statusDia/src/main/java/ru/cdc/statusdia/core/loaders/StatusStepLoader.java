package ru.cdc.statusdia.core.loaders;

import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.model.LinkCondition;
import ru.cdc.statusdia.core.model.StatusStep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StatusStepLoader {

    private static final String TAG = StatusStepLoader.class.getSimpleName();

    public static HashMap<Integer, ArrayList<StatusStep>> loadStatusStep() {
        Logger.info(TAG,"loading data from DS_ActivitiesSteps");

        HashMap<Integer, ArrayList<StatusStep>> list = new HashMap<>();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:support_optimum.db3");

            ResultSet c = connection.createStatement().executeQuery("select wa.ObjectTypeId, step.ParentRecordID, step.ParentStatusID, ifnull(av1.AttrValueName, '-'), " +
                    "step.ChildRecordID, step.ChildStatusID, av2.AttrValueName, " +
                    "step.UserType, step.FaceType, step.isPositive, step.isNegative, step.Navigation, step.Scan " +
                    "from DS_ActivitiesSteps step " +
                    "left join DS_WorkflowsActivities wa on wa.ActivityID = step.ActivityID " +
                    "left join DS_AttributesValues av1 on av1.AttrValueID = step.ParentStatusID and av1.attrID in (1727, 1104) " +
                    "left join DS_AttributesValues av2 on av2.AttrValueID = step.ChildStatusID and av2.attrID in (1727, 1104) " +
                    "order by step.ParentRecordID, step.ParentStatusID");

            while (c.next()) {
                int typeid = c.getInt(1);
                StatusStep item = new StatusStep(
                        typeid,
                        c.getInt(2),
                        c.getInt(3),
                        c.getString(4),
                        c.getInt(5),
                        c.getInt(6),
                        c.getString(7),
                        c.getInt(8),
                        c.getInt(9),
                        c.getInt(10),
                        c.getInt(11),
                        c.getInt(12),
                        c.getInt(13)
                );

                if (list.containsKey(typeid) == false) {
                    list.put(typeid, new ArrayList<>());
                }
                list.get(typeid).add(item);
            }
        } catch(SQLException e) {

            // if the error message is "out of memory",
            // it probably means no database file is found
            Logger.error(TAG, "Exception during loadStatusStep(): %s", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

                // connection close failed.
                Logger.error(TAG, "Exception during loadStatusStep(): %s", e);
            }
        }

        return list;
    }
}
