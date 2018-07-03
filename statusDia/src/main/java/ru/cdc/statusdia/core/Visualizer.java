package ru.cdc.statusdia.core;

import ru.cdc.statusdia.core.model.LinkCondition;
import ru.cdc.statusdia.core.model.StatusLink;
import ru.cdc.statusdia.core.model.StatusNode;
import ru.cdc.statusdia.core.model.StatusStep;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Visualizer {

    private static final String TAG = Visualizer.class.getSimpleName();

    private String linkStart;
    private String linkEnd;
    private String linkLine;
    private String linkLineCond;
    private String diaStart;
    private String diaEnd;
    private String statusStart;
    private String statusEnd;
    private String statusLine;


    public Visualizer() {
        linkStart = readFile("html/template_link_start.txt");
        linkEnd = readFile("html/template_link_end.txt");
        linkLine = readFile("html/template_link_line.txt");
        linkLineCond = readFile("html/template_link_conditions_line.txt");

        diaStart = readFile("html/template_dia_start.txt");
        diaEnd = readFile("html/template_dia_end.txt");

        statusStart = readFile("html/template_status_start.txt");
        statusEnd = readFile("html/template_status_end.txt");
        statusLine = readFile("html/template_status_line.txt");
    }


    private String readFile(String filename) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(Utils.getResourceAsStream(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (IOException e) {
            Logger.error(TAG, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Logger.error(TAG, e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    public void writeLinksToHtmlFile(String filename, ArrayList<StatusLink> links, HashMap<Integer, ArrayList<LinkCondition>> conds) {
        BufferedWriter fs = null;
        try {
            fs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            fs.append(linkStart);

            for (StatusLink link : links) {
                appendLinkLine(fs, link, conds.get(link.getLinkID()));
            }

            fs.append(linkEnd);
            fs.flush();
        } catch (IOException e) {
            Logger.error(TAG, e.getMessage());
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    Logger.error(TAG, e.getMessage());
                }
            }
        }
    }

    private void appendLinkLine(BufferedWriter fs, StatusLink link, ArrayList<LinkCondition> conds) throws IOException {

        fs.append(String.format(linkLine,
                link.getLinkID(),
                link.getType1(),
                link.getStatus1(),
                link.getStatusID1(), link.getRecordID1(),
                link.getType2(),
                link.getStatus2(),
                link.getStatusID2(), link.getRecordID2(),
                generateConditions(conds)
                ));
    }

    private String generateConditions(ArrayList<LinkCondition> conds) {
        if (conds == null || conds.size() == 0) {
            return "<i>нет условий</i>";
        }

        HashMap<String, StringBuffer> map = new HashMap<>();

        for (LinkCondition cond : conds) {
            if (map.containsKey(cond.getType()) == false) {
                map.put(cond.getType(), new StringBuffer());
            }
            map.get(cond.getType()).append("<li>").append(cond.getValue()).append("</li>");
        }

        StringBuffer sb = new StringBuffer();
        for (String key : map.keySet()) {
            sb.append(String.format(linkLineCond,
                    key,
                    map.get(key).toString()));
        }

        return sb.toString();
    }

    public void writeStepsToHtmlFile(String filename, ArrayList<StatusStep> steps, ArrayList<StatusLink> links) {
        BufferedWriter fs = null;
        try {
            fs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            fs.append(statusStart);

            for (StatusStep step : steps) {
                appendStepLine(fs, step);
            }

            fs.append(statusEnd);
            fs.flush();
        } catch (IOException e) {
            Logger.error(TAG, e.getMessage());
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    Logger.error(TAG, e.getMessage());
                }
            }
        }
    }

    private void appendStepLine(BufferedWriter fs, StatusStep step) throws IOException {

        fs.append(String.format(statusLine,
                step.getStatusType(),
                step.isAuto() ? "A" : "",
                step.getPlaceType(),
                step.getParent().getHtml(),
                step.getChild().getHtml(),
                step.getParams(),
                "автопереходы"
        ));
    }

    public void writeDiaToHtmlFile(String filename, ArrayList<StatusStep> steps) {
        BufferedWriter fs = null;
        try {
            fs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            fs.append(diaStart);

            ArrayList<StatusNode> nodes = getUniqueNodes(steps);

            // NODES
            StringBuffer sb = new StringBuffer();
            sb.append("var nodes = [");
            for (int i = 0; i < nodes.size(); i++) {
                sb.append(nodes.get(i).getDiaHtml(i));
            }
            sb.append("];");

            fs.append(sb.toString());

            // EDGES
            sb.setLength(0);
            sb.append("var edges = [");
            for (StatusStep step : steps) {
                int from = nodes.indexOf(step.getParent());
                int to = nodes.indexOf(step.getChild());
                sb.append(step.getHtml(from, to));
            }
            sb.append("];");

            fs.append(sb.toString());

            fs.append(diaEnd);
            fs.flush();
        } catch (IOException e) {
            Logger.error(TAG, e.getMessage());
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    Logger.error(TAG, e.getMessage());
                }
            }
        }
    }

    private ArrayList<StatusNode> getUniqueNodes(ArrayList<StatusStep> steps) {
        ArrayList<StatusNode> nodes = new ArrayList<>();

        for (StatusStep step : steps) {
            StatusNode parent = step.getParent();
            if (nodes.contains(parent) == false) {
                nodes.add(parent);
            }
        }

        for (StatusStep step : steps) {
            StatusNode child = step.getChild();
            if (nodes.contains(child) == false) {
                nodes.add(child);
            }
        }

        return nodes;
    }
}
