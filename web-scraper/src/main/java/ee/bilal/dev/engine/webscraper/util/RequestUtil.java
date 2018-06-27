package ee.bilal.dev.engine.webscraper.util;


import ee.bilal.dev.engine.webscraper.application.constants.AppGlobal;
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

    private RequestUtil() {
        throw new AssertionError();
    }

    /**
     * Wrapper for makeRequest
     * @param restTemplate for request exchange
     * @param url for request
     * @param body of request
     * @param method to use in request
     * @param headers http headers
     * @return request result
     */
    public static ResponseEntity<String> makeRequest (RestTemplate restTemplate, String url,
                                                      String body, HttpMethod method, HttpHeaders headers) {
        return makeRequest(restTemplate, url, body, null, method, headers);
    }

    /**
     * Make http request and get response
     * @param restTemplate for request exchange
     * @param url for request
     * @param body of request
     * @param params of request
     * @param method to use
     * @param headers http headers
     * @return request result
     */
    public static ResponseEntity<String> makeRequest (RestTemplate restTemplate, String url, String body,
                                                      MultiValueMap<String,String> params, HttpMethod method,
                                                      HttpHeaders headers) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(params);
            String uriBuilder = builder.build().encode().toUriString();
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(uriBuilder, method, entity, String.class);
        }catch (Exception e){
            LOGGER.error("Error making a {} request to {} with {} and {}",
                    method.toString(), url, body, params, e);

            throw e;
        }
    }

    /**
     * Create http response headers
     * @param username for authentication
     * @param password for authentication
     * @return created http headers
     */
    public static HttpHeaders createHeaders(String username, String password){
        String auth = username + AppGlobal.COLON_SEPARATOR + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(AppGlobal.CHARSET_US_ASCII)) );
        String authHeader = AppGlobal.AUTH_HEADER_BASIC + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AppGlobal.AUTHORIZATION, authHeader);
        headers.set( AppGlobal.CONTENT_TYPE, AppGlobal.CONTENT_TYPE_JSON);

        return headers;
    }

    /**
     * Get server base url
     * @param request http request
     * @return base url
     * @throws MalformedURLException where the url is not valid
     */
    public static URL getBaseUrl(HttpServletRequest request) throws MalformedURLException {
        try {
            return new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
                    request.getContextPath().concat(request.getRequestURI()));
        } catch (MalformedURLException e) {
            LOGGER.error("Error constructing the base url '{}'", e.getMessage());

            throw e;
        }
    }

    /**
     * Get file url on server
     * @param request http request
     * @param file the file on the host
     * @return formed url
     * @throws MalformedURLException where the url is not valid
     */
    public static URL getUrl(HttpServletRequest request, String file) throws MalformedURLException {
        try {
            return new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
                    file);
        } catch (MalformedURLException e) {
            LOGGER.error("Error constructing the base url '{}'", e.getMessage());

            throw e;
        }
    }
}
