package dev.forbit.server.old.logging;

import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Default formatter
 */
public class LogFormatter extends SimpleFormatter {

    /**
     * String used for formatting.
     */
    private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

    @Override public synchronized String format(LogRecord lr) {
        return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(), lr.getMessage());
    }

}
