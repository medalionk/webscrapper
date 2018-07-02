package ee.bilal.dev.engine.webscraper.application.constants;

public class Paths {
    private Paths()
    {
        throw new AssertionError();
    }

    public static final String VERSION = "/api/1.0";
    public static final String JOBS = "/jobs";
    public static final String REPORT = JOBS + "/report";
    public static final String STOP_ALL = JOBS + "/stop-all";
}
