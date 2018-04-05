package ee.bilal.dev.engine.webscraper.application.exceptions;

public class InvalidArgumentServiceException extends ServiceException {

  private static final long serialVersionUID = 1L;
  private final String fieldName;
  private final String errorMessage;

  public InvalidArgumentServiceException(String fieldName, String errorMessage) {
    super("invalidArgument: field=" + fieldName + " error=" + errorMessage);
    this.fieldName = fieldName;
    this.errorMessage = errorMessage;
  }
  
  public String getFieldName() {
    return fieldName;
  }
  
  public String getErrorMessage() {
    return errorMessage;
  }
}
