package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 9/10/2017.
 */
public class AuthorizationException extends Exception {
    public AuthorizationException() {
        this("Authorization error!");
    }

    public AuthorizationException(String message) {
        this(message, (Throwable)null);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
