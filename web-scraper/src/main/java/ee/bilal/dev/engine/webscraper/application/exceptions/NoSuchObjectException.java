package ee.bilal.dev.engine.webscraper.application.exceptions;


public class NoSuchObjectException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchObjectException(String name) {
    super("Object: " + name + "not found!!!");
  }

  public NoSuchObjectException() {
    super("Object not found");
  }
}
