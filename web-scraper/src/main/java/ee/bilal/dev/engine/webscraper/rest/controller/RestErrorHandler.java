package ee.bilal.dev.engine.webscraper.rest.controller;

import ee.bilal.dev.engine.webscraper.application.exceptions.ServiceException;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestErrorHandler {
    private final Logger logger;

    @Autowired
    public RestErrorHandler() {
        this.logger = LoggerFactory.getLogger(RestErrorHandler.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String,String>> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<ObjectError> errors = result.getAllErrors();
        List<String> strErrors = errors.stream()
                .map(error -> "Field: " + error.getCode() + "; Error: " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.BAD_REQUEST,
                new IllegalArgumentException(strErrors.toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Invalid parameters: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String,String>> handleIllegalStateException(IllegalStateException ex) {
        logger.error("Invalid state: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("The resource was not found: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(MalformedURLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String,String>> handleMalformedURLException(MalformedURLException ex) {
        logger.error("Malformed URL: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String,String>> handleDataAccessException(DataAccessException ex) {
        logger.error("Data access error: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String,String>> handleServiceException(ServiceException ex) {
        logger.error("Service error: {}", ex.getMessage());

        return ResponseUtil.exceptionResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
