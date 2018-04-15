package ee.bilal.dev.engine.webscraper.util;

import ee.bilal.dev.engine.webscraper.application.exceptions.ConfigurationException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by bilal90 on 9/7/2017.
 */
public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> void validateConfigPropertyNotNull(T property, String name)
            throws ConfigurationException {
        if (property == null)
        {
            throw new ConfigurationException(
                    String.format("Configurable property '%s' cannot be null.", name));
        }
    }

    public static <T> void validatePropertyNotNull(T t, String name) {
        if (t == null) throw new IllegalArgumentException(String.format("Parameter '%s' cannot be null", name));
    }

    public static <T> void validateEntity(T entity)
    {
        validatePropertyNotNull(entity, "entity");
    }

    public static void validateIdentity(String id)
    {
        validateStringNotNullOrEmpty(id, "Id");
    }

    public static <E> void validateListNotNullOrEmpty(List<E> property, String name) {
        validatePropertyNotNull(property, name);
        if (property.isEmpty())
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot be empty", name));
    }

    public static void validateStringNotNullOrEmpty(String property, String name) {
        validatePropertyNotNull(property, name);
        if (property.trim().isEmpty())
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot be empty", name));
    }

    public static void validateDateIsNowOrFuture(LocalDate date) {
        if (ZonedDateTime.now().toLocalDate().isAfter(date)) throw new IllegalArgumentException("Date cannot be in the past");
    }
}
