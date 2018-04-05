package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 10/26/2017.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
