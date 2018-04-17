package ee.bilal.dev.engine.webscraper.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ResponseUtil {
    private ResponseUtil() {
    }

    public static ResponseEntity<Map<String,String>> exceptionResponseBuilder(HttpStatus status, Exception ex){
        Map<String,String> response = new HashMap<>();

        response.put("timestamp", getTimeStamp());
        response.put("status", String.valueOf(status.value()));
        response.put("error", status.getReasonPhrase());
        response.put("exception", ex.getClass().getName());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, status);
    }

    public static String getTimeStamp(){
        long time = new Timestamp(System.currentTimeMillis()).getTime();
        return String.valueOf(time);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, (HttpHeaders)null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map((ResponseEntity.ok().headers(header))::body)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
