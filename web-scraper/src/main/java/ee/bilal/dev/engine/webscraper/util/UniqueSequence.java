package ee.bilal.dev.engine.webscraper.util;

import java.util.concurrent.atomic.AtomicLong;

public class UniqueSequence {
    private static final AtomicLong sequence = new AtomicLong(System.currentTimeMillis());

    public static String getNext() {
        return String.valueOf(sequence.incrementAndGet());
    }
}
