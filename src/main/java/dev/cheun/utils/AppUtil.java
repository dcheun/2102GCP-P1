// Misc App Utilities.

package dev.cheun.utils;

import org.apache.log4j.Logger;

public class AppUtil {
    /**
     * Helper method to convert stack trace returned from e.getStackTrace()
     * into a String that can be logged.
     *
     * @param trace     array of stack trace elements, pass in e.getStrackTrace()
     * @return          the stack trace elements converted to a String.
     */
    public static String stackTraceElementsToString(StackTraceElement[] trace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement traceElement : trace) {
            sb.append("\tat " + traceElement + "\n");
        }
        return sb.toString();
    }

    /**
     * Helper method to log exceptions and user messages to a log file.
     *
     * @param logger    the log4j logger object to call.
     * @param e         the caught exception.
     * @param msg       a user message prepended to the front.
     */
    public static void logException(Logger logger, Exception e, String msg) {
        logger.error(
                msg + "\n" +
                e.getClass().getCanonicalName() + ": " +
                e.getMessage() + "\n" +
                stackTraceElementsToString(e.getStackTrace()));
    }
}
