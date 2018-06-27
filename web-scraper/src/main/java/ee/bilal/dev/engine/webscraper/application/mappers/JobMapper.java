package ee.bilal.dev.engine.webscraper.application.mappers;

import org.mapstruct.factory.Mappers;

public final class JobMapper {

  private JobMapper()
  {
    throw new AssertionError();
  }

  public static final JobReportMapper JOB_REPORT_MAPPER = Mappers.getMapper(JobReportMapper.class );
  public static final JobRequestMapper JOB_REQUEST_MAPPER = Mappers.getMapper(JobRequestMapper.class );
  public static final JobResultMapper JOB_RESULT_MAPPER = Mappers.getMapper(JobResultMapper.class );
}
