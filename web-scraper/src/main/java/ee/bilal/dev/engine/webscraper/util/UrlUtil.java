package ee.bilal.dev.engine.webscraper.util;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtil.class);

    public static boolean isValidUrl(final String url, final String[] schemes){
        final UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

    public static boolean isSameHost(String url1, String url2) {
        try {
            URI uri1 = new URI(url1);
            URI uri2 = new URI(url2);

            return uri1.getHost().equalsIgnoreCase(uri2.getHost());
        } catch (URISyntaxException e) {
            LOGGER.error("Error creating URI!!!", e);

            return false;
        }
    }
}
