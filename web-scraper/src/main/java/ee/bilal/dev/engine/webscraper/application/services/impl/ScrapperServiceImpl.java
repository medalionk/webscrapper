package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobReportService;
import ee.bilal.dev.engine.webscraper.application.services.ScrapperService;
import ee.bilal.dev.engine.webscraper.configurations.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ScrapperServiceImpl implements ScrapperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperServiceImpl.class);

    private final JobReportService reportService;
    private final ApplicationConfig config;

    @Autowired
    public ScrapperServiceImpl(JobReportService reportService, ApplicationConfig config) {
        this.reportService = reportService;
        this.config = config;
    }

    @Async
    @Override
    public void scrape(List<JobRequestDTO> reqs, Consumer<JobResultDTO> consumer) {
        final long start = System.nanoTime();

        for (JobRequestDTO req : reqs) {
            LOGGER.info("Job request for: '{}' ", req);

            final String jobId = req.getId();
            final int linksPerLevel = req.getLinksPerLevel();
            final double approxTotal = Math.pow(linksPerLevel, req.getMaxLevel()) + linksPerLevel + 1;
            final float progress = (float)Math.ceil((100 / approxTotal) * 10) / 10;

            Runnable progressHandler = () -> {
                LOGGER.info("\nUpdate progress for Job: ({})", jobId);
                reportService.updateProgress(jobId, progress, JobStatusDTO.ONGOING);
            };

            final ScrapperForkJoin scrapper = new ScrapperForkJoin(req, consumer, progressHandler);
            scrapper.scrapePage();

            LOGGER.info("Mark Job '{}' as completed ", jobId);
            reportService.markCompleted(jobId);

            final double taken = (System.nanoTime() - start) / 1000000000.0;
            LOGGER.warn("Completed in: '{}' seconds", taken);
        }
    }

    @Override
    public void stopAll() {

    }

    /**
     * Update reports to indicate cancellation
     */
    private void updateCanceledReports() {
        reportService
                .findAll()
                .stream()
                .filter(x -> x.getStatus() == JobStatusDTO.CREATED)
                .forEach(x -> reportService.updateStatus(x.getId(), JobStatusDTO.CANCELED));
    }
}
