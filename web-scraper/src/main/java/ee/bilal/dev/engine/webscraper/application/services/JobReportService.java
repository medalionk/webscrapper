package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;

import java.util.Map;
import java.util.Optional;

public interface JobReportService extends GenericService<JobReportDTO> {
    /**
     * Update progress and status of JobReport with given id
     * @param id of Job
     * @param progress new Job progress
     * @param status Current Job status
     * @return updated JobReport
     */
    Optional<JobReportDTO> updateProgress(String id, float progress, JobStatusDTO status);

    /**
     * Update status of JobReport with given id
     * @param id of Job
     * @param status Current Job status
     * @return updated JobReport
     */
    Optional<JobReportDTO> updateStatus(String id, JobStatusDTO status);

    /**
     * Set job as completed
     * @param id of Job
     * @return updated JobReport
     */
    Optional<JobReportDTO> markCompleted(String id);

    /**
     * Get Jobs statuses
     * @return Jobs report
     */
    Map<String,Object> getStatus();
}
