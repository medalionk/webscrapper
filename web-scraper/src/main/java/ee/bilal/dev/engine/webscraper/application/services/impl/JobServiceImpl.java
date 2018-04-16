package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.services.*;
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
    private final JobRequestService requestService;
    private final JobResultService resultService;
    private final JobReportService reportService;

    @Autowired
    public JobServiceImpl(ScrapperService scrapperService, JobResultService resultService,
                          JobReportService reportService, JobRequestService requestService) {
        this.scrapperService = scrapperService;
        this.requestService = requestService;
        this.resultService = resultService;
        this.reportService = reportService;
    }

    @Override
    public List<JobReportDTO> getAllJobs() {
        return reportService.findAll();
    }

    @Override
    public Optional<JobReportDTO> getJob(String id) {
        return reportService.findOne(id);
    }

    @Override
    public List<JobReportDTO> processJobs(List<JobRequestDTO> requests) {
        List<JobRequestDTO> savedRequests = requestService.saveAll(requests);
        Consumer<JobResultDTO> consumer = x -> {
            LOGGER.info("\nResult: ({})", x);
            resultService.create(x);
        };

        scrapperService.scrapeSync(savedRequests, consumer);
        return new ArrayList<>();
    }
}
