package ee.bilal.dev.engine.webscraper.application.exceptions;

public class AuthenticationException extends Exception {
    public AuthenticationException() {
        this("Authentication error!");
    }

    public AuthenticationException(String message) {
        this(message, (Throwable)null);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
