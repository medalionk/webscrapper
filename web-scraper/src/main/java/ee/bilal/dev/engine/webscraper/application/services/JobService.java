package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by bilal90 on 5/4/2018.
 */
public interface JobService {
    List<JobReportDTO> getAllJobs();
    Optional<JobReportDTO> getJob(String jobId);
    List<JobReportDTO> processJobs(List<JobRequestDTO> requests);
}
