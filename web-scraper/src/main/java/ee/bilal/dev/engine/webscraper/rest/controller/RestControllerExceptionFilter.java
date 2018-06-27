package ee.bilal.dev.engine.webscraper.rest.controller;

import ee.bilal.dev.engine.webscraper.application.exceptions.ServiceException;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.MalformedURLException;
import java.util.Map;

/**
 * Created by bilal90 on 5/4/2018.
 */
public class RestControllerExceptionFilter {
    protected final Logger logger;

    protected <U extends RestControllerExceptionFilter> RestControllerExceptionFilter(Class<U> tClass) {
        this.logger = LoggerFactory.getLogger(tClass);
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
