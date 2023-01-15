package fr.unice.polytech.si3.qgl.kihm.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * Class that makes our display
 */
public class Printer {

    /**
     * The instance of the class Printer, used to have only one object Printer
     */
    private static Printer instance;

    /**
     * The logger
     */
    private final Logger log;
    private final List<String> logs;
    /**
     * The level of the messages to display
     */
    private Level logLevel = Level.INFO;

    /**
     * The constructor of the class Printer
     */
    private Printer() {
        logs = new ArrayList<>();
        this.log = Logger.getLogger(Printer.class.getName());
        Handler customHandler = new StreamHandler(System.out, new CustomFormatter());
        customHandler.setLevel(Level.ALL);
        this.log.addHandler(customHandler);
        this.log.setLevel(this.logLevel);
        this.log.setUseParentHandlers(false);
    }

    /**
     * Used to get the instance of the printer
     *
     * @return the instance of the printer
     */
    public static Printer get() {
        if (instance == null) {
            instance = new Printer();
        }
        return instance;
    }

    public List<String> getLogs() {
        return this.logs;
    }

    public String getLastLog() {
        return this.logs.get(this.logs.size() - 1);
    }

    public Level getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(Level level) {
        this.logLevel = level;
        this.levelUpdate();
    }

    /**
     * Change the level of the log to the logLevel
     */
    private void levelUpdate() {
        this.log.setLevel(this.logLevel);
    }

    private void addToLogs(Object message) {
        this.logs.add(message.toString());
    }

    public void finest(Object message) {
        this.log.finest(message::toString);
        addToLogs(message);
    }

    public void finer(Object message) {
        this.log.finer(message::toString);
        addToLogs(message);
    }


    public void fine(Object message) {
        this.log.fine(message::toString);
        addToLogs(message);
    }

    public void config(Object message) {
        this.log.config(message::toString);
        addToLogs(message);
    }

    public void info(Object message) {
        this.log.info(message::toString);
        addToLogs(message);
    }

    public void warning(Object message) {
        this.log.warning(message::toString);
        addToLogs(message);
    }

    public void severe(Object message) {
        this.log.severe(message::toString);
        addToLogs(message);
    }

    public void flush() {
        this.log.getHandlers()[0].flush();
    }
}
