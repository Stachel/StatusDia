package ru.cdc.statusdia.core;

public class Logger {

    public enum Level {
        Verbose(2),
        Debug(3),
        Info(4),
        Warn(5),
        Error(6),
        Assert(7);

        private final int _level;

        Level(int level) {
            _level = level;
        }

        public static Level getLevel(int level) {
            for (Level l : values()) {
                if (l._level == level) {
                    return l;
                }
            }
            return Verbose;
        }

        public String getTag() {
            return name().toUpperCase();
        }

        public int getLevel() {
            return _level;
        }
    }

    public interface ILogger {
        void println(int priority, String extraTag, String msg, Object[] params, Throwable tr);
    }

    private static ILogger _logger;

    public static void setLogger(ILogger logger) {
        _logger = logger;
    }

    public static void error(String extraTag, String message, Throwable tr) {
        if (_logger != null) {
            _logger.println(Level.Error.getLevel(), extraTag, message, null, tr);
        }
    }

    public static void warn(String extraTag, String message, Throwable tr) {
        if (_logger != null) {
            _logger.println(Level.Warn.getLevel(), extraTag, message, null, tr);
        }
    }

    public static void info(String extraTag, String message, Throwable tr) {
        if (_logger != null) {
            _logger.println(Level.Info.getLevel(), extraTag, message, null, tr);
        }
    }

    public static void debug(String extraTag, String message, Throwable tr) {
        if (_logger != null) {
            _logger.println(Level.Debug.getLevel(), extraTag, message, null, tr);
        }
    }

    public static void error(String extraTag, String message, Object... params) {
        if (_logger != null) {
            _logger.println(Level.Error.getLevel(), extraTag, message, params, null);
        }
    }

    public static void warn(String extraTag, String message, Object... params) {
        if (_logger != null) {
            _logger.println(Level.Warn.getLevel(), extraTag, message, params, null);
        }
    }

    public static void info(String extraTag, String message, Object... params) {
        if (_logger != null) {
            _logger.println(Level.Info.getLevel(), extraTag, message, params, null);
        }
    }

    public static void debug(String extraTag, String message, Object... params) {
        if (_logger != null) {
            _logger.println(Level.Debug.getLevel(), extraTag, message, params, null);
        }
    }
}
