package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobResultService;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.application.services.ScrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    private final ScrapperService scrapperService;
    private final JobResultService resultService;

    @Autowired
    public JobServiceImpl(ScrapperService scrapperService, JobResultService resultService) {
        this.scrapperService = scrapperService;
        this.resultService = resultService;
    }

    @Override
    public List<JobReportDTO> getAllJobs() {
        return new ArrayList<>();
    }

    @Override
    public Optional<JobReportDTO> getJob(String jobId) {
        return null;
    }

    @Override
    public List<JobReportDTO> processJobs(List<JobRequestDTO> requests) {
        Consumer<JobResultDTO> consumer = x -> {
            LOGGER.info("\nResult: ({})", x);
            resultService.create(x);
        };
        scrapperService.scrapeSync(requests, consumer);
        return new ArrayList<>();
    }
}
