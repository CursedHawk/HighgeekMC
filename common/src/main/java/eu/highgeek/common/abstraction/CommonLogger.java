package eu.highgeek.common.abstraction;

public interface CommonLogger {
    void info(String message);
    void warn(String message);
    void severe(String message);
    void debug(String message);
    boolean debug();
}
