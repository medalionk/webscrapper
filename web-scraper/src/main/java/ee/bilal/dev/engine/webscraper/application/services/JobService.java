package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by bilal90 on 5/4/2018.
 */
public interface JobService {
    /**
     * Get all Jobs
     * @return list of JobReport
     */
    List<JobReportDTO> getAllJobs();

    /**
     * Get Report for Job with given ID
     * @param id of Job
     * @return JobReport for job
     */
    Optional<JobReportDTO> getJob(String id);

    /**
     * Process list of Jobs
     * @param requests list of Jobs to process
     * @return list of JobReport for each Jobn
     */
    List<JobReportDTO> processJobs(List<JobRequestDTO> requests);

    /**
     * Get Jobs statuses
     * @return Jobs report
     */
    Map<String,Object> getReport();

    /**
     * Stop all ongoing jobs
     */
    void stopOngoingJobs();
}
