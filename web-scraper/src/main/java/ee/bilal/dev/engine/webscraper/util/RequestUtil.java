package ee.bilal.dev.engine.webscraper.util;


import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by bilal90 on 9/27/2017.
 */
public final class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static ResponseEntity<String> makeRequest (
            RestTemplate restTemplate, String url, String body, HttpMethod method, HttpHeaders headers) {
        return makeRequest(restTemplate, url, body, method, null);
    }

    public static ResponseEntity<String> makeRequest (
            RestTemplate restTemplate, String url, String body, MultiValueMap<String,String> params,
            HttpMethod method, HttpHeaders headers) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(params);
            String uriBuilder = builder.build().encode().toUriString();

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            return restTemplate.exchange(uriBuilder, method, entity, String.class);
        }catch (Exception e){
            LOGGER.error("Error making a {} request to {} with {} and {}",
                    method.toString(), url, body, params, e);
            return null;
        }
    }

    public static HttpHeaders createHeaders(String username, String password){
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set( "Content-Type", "application/json;charset=UTF-8" );

        return headers;
    }

    public static URL getBaseUrl(HttpServletRequest request){
        try {
            return new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
                    request.getContextPath().concat(request.getRequestURI()));
        } catch (MalformedURLException e) {
            LOGGER.error("Error constructing the base url '{}'", e.getMessage());
            return null;
        }
    }

    public static URL getUrl(HttpServletRequest request, String file){
        try {
            return new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
                    file);
        } catch (MalformedURLException e) {
            LOGGER.error("Error constructing the base url '{}'", e.getMessage());
            return null;
        }
    }
}
