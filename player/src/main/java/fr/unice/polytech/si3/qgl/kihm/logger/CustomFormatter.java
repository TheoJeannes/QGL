package fr.unice.polytech.si3.qgl.kihm.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class that makes the custom colors
 */
public class CustomFormatter extends Formatter {
    public static final String RESET = "\033[0m";
    // Regular Colors
    public static final String RED_COLOR = "\033[0;31m";
    public static final String GREEN_COLOR = "\033[0;32m";
    public static final String YELLOW_COLOR = "\033[0;33m";
    public static final String BLUE_COLOR = "\033[0;34m";
    public static final String PURPLE_COLOR = "\033[0;35m";
    public static final String CYAN_COLOR = "\033[0;36m";
    public static final String WHITE_COLOR = "\033[0;37m";
    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";
    public static final String RED_BRIGHT = "\033[0;91m";
    public static final String GREEN_BRIGHT = "\033[0;92m";
    public static final String YELLOW_BRIGHT = "\033[0;93m";
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String CYAN_BRIGHT = "\033[0;96m";
    private final Date date = new Date();

    @Override
    public String format(LogRecord logRecord) {
        String colorLevel = switch (logRecord.getLevel().toString().toUpperCase()) {
            case "SEVERE" -> RED_BRIGHT;
            case "WARNING" -> RED_COLOR;
            case "INFO" -> YELLOW_BRIGHT;
            case "CONFIG" -> YELLOW_COLOR;
            case "FINE" -> BLUE_COLOR;
            case "FINER" -> GREEN_BRIGHT;
            case "FINEST" -> GREEN_COLOR;
            case "ALL" -> PURPLE_BRIGHT;
            default -> BLACK_BRIGHT;
        };

        String[] lines = logRecord.getMessage().split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        String s;
        for (String line : lines) {
            date.setTime(logRecord.getMillis());
            String dateString = "[" + new SimpleDateFormat("dd/MM/yyyy").format(date) + " " + new SimpleDateFormat("HH:mm:ss").format(date) + "]";
            s = RED_COLOR + "%1$s" + colorLevel + "%2$8s" + RESET + "   %3$s%n";
            stringBuilder.append(String.format(s, dateString, "[" + logRecord.getLevel().getLocalizedName() + "]", line));
        }
        return stringBuilder.toString();
    }
}