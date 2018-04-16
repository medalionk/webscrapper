package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;

import java.util.Optional;

public interface JobReportService extends GenericService<JobReportDTO> {
    JobReportDTO updateProgress(String id, float progress);
}
