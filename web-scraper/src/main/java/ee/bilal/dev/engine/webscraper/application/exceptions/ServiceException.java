package ee.bilal.dev.engine.webscraper.application.exceptions;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ServiceException() {

  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Throwable ex) {
    super(ex);
  }
  
  public ServiceException(String message, Throwable ex) {
    super(message, ex);
  }
}
