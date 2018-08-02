package ru.cdc.statusdia;

import org.apache.commons.cli.*;
import ru.cdc.statusdia.core.Logger;
import ru.cdc.statusdia.core.Utils;
import ru.cdc.statusdia.core.Visualizer;
import ru.cdc.statusdia.core.loaders.*;
import ru.cdc.statusdia.core.model.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;


public class StatusDiaApplication {

    private static final String TAG = StatusDiaApplication.class.getSimpleName();

    public static void main(String[] args) {

        // Handle arguments
        Options options = makeOptions();

        CommandLine cmd;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("status-dia", options);

            System.exit(1);
            return;
        }

        /*if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("status-dia", options);

            System.exit(1);
            return;
        }*/

        // Main program
        Logger.setLogger(Utils.makeLogger());
        Logger.info(TAG, "launched");

        // Load all data
        ArrayList<StatusLink> links = StatusLinkLoader.loadStatusLink();
        HashMap<Integer, ArrayList<LinkCondition>> conds = LinkConditionLoader.loadLinkCondition();
        HashMap<Integer, ArrayList<StatusStep>> status = StatusStepLoader.loadStatusStep();
        HashMap<Integer, ArrayList<StatusAttr>> docStatuses = StatusAttrLoader.loadStatusAttr();
        HashMap<Integer, SecondStatus> secondStatuses = SecondStatusLoader.loadSecondStatus();

        Visualizer visualizer = new Visualizer();

        // Status links
        if (links.size() > 0) {
            File linkFile = new File("links.html");
            visualizer.writeLinksToHtmlFile(linkFile.getAbsolutePath(), links, conds);
        }

        // Status dia
        /*for (Integer type : status.keySet()) {
            File linkFile = new File(type > 1000 ? "route.html" : "doc_" + type + ".html");
            visualizer.writeDiaToHtmlFile(linkFile.getAbsolutePath(), status.get(type));
        }*/

        // Status table
        for (Integer type : status.keySet()) {
            File linkFile = new File(type > 1000 ? "route.html" : "doc_" + type + ".html");
            visualizer.writeStepsToHtmlFile(linkFile.getAbsolutePath(), status.get(type), links,  docStatuses, type > 1000);
        }

        // Second status table
        for (Integer type : secondStatuses.keySet()) {
            File linkFile = new File("second_" + type + ".html");
            visualizer.writeSecondToHtmlFile(linkFile.getAbsolutePath(), secondStatuses.get(type));
        }

        Logger.info(TAG, "done");
    }

    private static Options makeOptions() {
        Options options = new Options();

        /*Option help = new Option("h", "help", false, "show help");
        help.setRequired(false);
        options.addOption(help);*/

        return options;
    }
}