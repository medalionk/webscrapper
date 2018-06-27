package ee.bilal.dev.engine.webscraper.application.exceptions;

/**
 * Created by bilal90 on 10/26/2017.
 */
public class FileNotFoundException extends StorageException {
    private static final long serialVersionUID = 1L;

    public FileNotFoundException() {
        this("File not found error!");
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
