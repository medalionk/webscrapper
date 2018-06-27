package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 9/10/2017.
 */
public class AuthorizationException extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthorizationException() {
        super("Authorization error!");
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
