package ee.bilal.dev.engine.webscraper.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bilal90 on 9/6/2017.
 */
public class IdFactory {
    private IdFactory() {
    }

    private static final AtomicLong sequence = new AtomicLong(System.currentTimeMillis());
    public static String uuidID() {
        return UUID.randomUUID().toString();
    }

    public static String uniqueSequence() {
        return String.valueOf(sequence.incrementAndGet());
    }
}
