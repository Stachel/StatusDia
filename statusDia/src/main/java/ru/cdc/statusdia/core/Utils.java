package ru.cdc.statusdia.core;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final String DATE_TIME = "dd-MM-yyyy HH:mm:ss";

    public static String formatDate(Date date, String timezone) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat(DATE_TIME);
            if (timezone != null) {
                dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            }
            return dateFormat.format(date);
        }
        return "";
    }

    public static String formatDate(long timestamp, String timezone ) {
        return formatDate(new Date(timestamp), timezone);
    }

    public static Date parseDate(String s, String timezone) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME);
        if (timezone != null) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            Logger.error(TAG, e.getMessage());
        }
        return date;
    }

    public static InputStream getResourceAsStream(String filepath) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        return cl.getResourceAsStream(filepath);
    }

    public static String getResourcePath(String filepath) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL url = cl.getResource(filepath);
        return url != null ? url.getFile() : "";
    }

    public static String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        return extension;
    }

    public static Date makeDate(int year, int month, int day, int hour, int minute, int second, String timezone) {
        Calendar cal = GregorianCalendar.getInstance();
        if (timezone != null) {
            cal.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static double to(Date date, String timezone) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        if (timezone != null) {
            calendar.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        int Y = calendar.get(GregorianCalendar.YEAR);
        int M = calendar.get(GregorianCalendar.MONTH) + 1;
        int D = calendar.get(GregorianCalendar.DAY_OF_MONTH);

        if (M <= 2) {
            Y--;
            M += 12;
        }
        int A = Y / 100;
        int B = 2 - A + (A / 4);
        int X1 = (int) (365.25 * (Y + 4716));
        int X2 = (int) (30.6001 * (M + 1));
        double jdt = X1 + X2 + D + B - 1524.5;

        int h = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int m = calendar.get(GregorianCalendar.MINUTE);
        int s = calendar.get(GregorianCalendar.SECOND);
        jdt += (h * 3600.0 + m * 60.0 + s) / 86400.0;
        return jdt;
    }

    public static Date from(double jdt, String timezone) {
        // Extract date:
        int Z = (int) (jdt + 0.5);
        int A = (int) ((Z - 1867216.25) / 36524.25);
        A = Z + 1 + A - (A / 4);
        int B = A + 1524;
        int C = (int) ((B - 122.1) / 365.25);
        int D = (int) (365.25 * C);
        int E = (int) ((B - D) / 30.6001);
        int X1 = (int) (30.6001 * E);
        D = B - D - X1;
        int MN = E < 14 ? E - 1 : E - 13;
        int Y = MN > 2 ? C - 4716 : C - 4715;

        //Extract time:
        Z = (int) (jdt + 0.5);
        int s = (int) ((jdt + 0.5 - Z) * 86400000.0 + 0.5);
        double S = 0.001 * s;
        s = (int) (S);
        S -= s;
        int H = s / 3600;
        s -= H * 3600;
        int M = s / 60;
        S += s - M * 60;
        return makeDate(Y, MN - 1, D, H, M, (int) S, timezone);
    }

    public static Logger.ILogger makeLogger() {
        return (priority, extraTag, msg, params, tr) -> {
            String log = "";

            log += "[" + Logger.Level.getLevel(priority).getTag() + "]";

            log += "[" + extraTag + "]";

            log += " " + String.format(msg, params);

            if (tr != null) {
                log += " " + tr.getMessage();
            }

            System.out.println(log);
        };
    }

    public static String removeComments(String json) {
        StringBuilder builder = new StringBuilder();

        String[] lines = json.split("\n");
        for (String line : lines) {
            int offset = line.indexOf("//");
            if (offset != -1) {
                line = line.substring(0, offset);
            }
            builder.append(line);
        }

        return builder.toString();
    }

    public static String encode(String str) {
        try {
            String v = new String(str.getBytes(), Charset.forName("UTF-8"));
            return new String(v);
        } catch (Exception e) {}
        return "";
    }

}
