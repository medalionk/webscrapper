package ee.bilal.dev.engine.webscraper.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtil {
    public static boolean isValidUrl(final String url, final String[] schemes){
        final UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

    public static boolean isSameHost(String url1, String url2) throws URISyntaxException {
        URI uri1 = new URI(url1);
        URI uri2 = new URI(url2);

        return uri1.getHost().equalsIgnoreCase(uri2.getHost());
    }
}
