package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 10/26/2017.
 */
public class FileNotFoundException extends StorageException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
