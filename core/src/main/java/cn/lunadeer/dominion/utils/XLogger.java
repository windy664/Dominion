package cn.lunadeer.dominion.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

import static cn.lunadeer.dominion.utils.Misc.formatString;

public class XLogger {
    public static XLogger instance;

    public XLogger() {
        instance = this;
        this._logger = Logger.getLogger("Lunadeer");
    }

    public XLogger(@Nullable JavaPlugin plugin) {
        instance = this;
        this._logger = plugin != null ? plugin.getLogger() : Logger.getLogger("Lunadeer");
    }

    public static XLogger setDebug(boolean debug) {
        instance._debug = debug;
        return instance;
    }

    public static boolean isDebug() {
        return instance._debug;
    }

    private final Logger _logger;
    private boolean _debug = false;

    public static void info(String message) {
        instance._logger.info("§a I | " + message);
    }

    public static void warn(String message) {
        instance._logger.warning("§e W | " + message);
    }

    public static void error(String message) {
        instance._logger.severe("§c E | " + message);
    }

    public static void debug(String message) {
        if (!instance._debug) return;
        instance._logger.info("§9 D | " + message);
    }

    public static void info(String message, Object... args) {
        info(String.format(message, args));
    }

    public static void warn(String message, Object... args) {
        warn(formatString(message, args));
    }

    public static void error(String message, Object... args) {
        error(formatString(message, args));
    }

    public static void error(Throwable e) {
        error(e.getMessage());
        if (isDebug()) {
            for (StackTraceElement element : e.getStackTrace()) {
                error("StackTrace | " + element.toString());
            }
        }
    }

    public static void debug(String message, Object... args) {
        debug(formatString(message, args));
    }
}
