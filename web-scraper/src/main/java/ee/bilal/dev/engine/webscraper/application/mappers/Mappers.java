package ee.bilal.dev.engine.webscraper.application.mappers;

public final class Mappers {

  // Not to be instantiated (with c'tor being private)
  private Mappers()
  {
    // According to <http://www.javapractices.com/topic/TopicAction.do?Id=2>:
    // "this prevents even the native class from calling this c'tor as well"
    throw new AssertionError();
  }

  public static final JobReportMapper JOB_REPORT_MAPPER = org.mapstruct.factory.Mappers.getMapper(JobReportMapper.class );
  public static final JobRequestMapper JOB_REQUEST_MAPPER = org.mapstruct.factory.Mappers.getMapper(JobRequestMapper.class );
  public static final JobResultMapper JOB_RESULT_MAPPER = org.mapstruct.factory.Mappers.getMapper(JobResultMapper.class );
}
