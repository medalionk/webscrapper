package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 10/26/2017.
 */
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
