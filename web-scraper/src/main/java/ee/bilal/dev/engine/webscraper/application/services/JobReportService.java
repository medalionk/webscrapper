package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;

import java.util.Map;
import java.util.Optional;

public interface JobReportService extends GenericService<JobReportDTO> {
    Optional<JobReportDTO> updateProgress(String id, float progress, JobStatusDTO status);
    Optional<JobReportDTO> updateStatus(String id, JobStatusDTO status);
    Optional<JobReportDTO> markCompleted(String id);
    Map<String,Object> getStatus();
}
