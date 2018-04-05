package ee.bilal.dev.engine.webscraper.application.exceptions;

public class ConfigurationException extends Exception {
    public ConfigurationException() {
        this("Configuration error!");
    }

    public ConfigurationException(String message) {
        this(message, (Throwable)null);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
