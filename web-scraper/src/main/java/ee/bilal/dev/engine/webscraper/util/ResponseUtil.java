package ee.bilal.dev.engine.webscraper.util;

import ee.bilal.dev.engine.webscraper.application.constants.AppGlobal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by bilal90 on 9/27/2017.
 */
public final class ResponseUtil {
    private ResponseUtil() {
        throw new AssertionError();
    }

    /**
     * Build error response
     * @param status http status
     * @param ex exception
     * @return built response
     */
    public static ResponseEntity<Map<String,String>> exceptionResponseBuilder(HttpStatus status, Exception ex){
        Map<String,String> response = new HashMap<>();

        response.put(AppGlobal.TIMESTAMP, getTimeStamp());
        response.put(AppGlobal.STATUS, String.valueOf(status.value()));
        response.put(AppGlobal.ERROR, status.getReasonPhrase());
        response.put(AppGlobal.EXCEPTION, ex.getClass().getName());
        response.put(AppGlobal.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(response, status);
    }

    /**
     * Get current time stamp
     * @return time stamp as string
     */
    public static String getTimeStamp(){
        long time = new Timestamp(System.currentTimeMillis()).getTime();

        return String.valueOf(time);
    }

    /**
     * Create response
     * @param maybeResponse optional response
     * @param <X> optional response type
     * @return built response
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, (HttpHeaders)null);
    }

    /**
     * Create response with header
     * @param maybeResponse optional response
     * @param header response header
     * @param <X> optional response type
     * @return built response
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map((ResponseEntity.ok().headers(header))::body)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
