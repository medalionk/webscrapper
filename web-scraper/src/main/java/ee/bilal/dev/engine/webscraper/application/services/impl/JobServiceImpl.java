package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.application.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    private static final float ZERO_PERCENT = 0.0f;

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
            LOGGER.info("\nSave result: ({})", x);
            resultService.create(x);
        };

        List<JobReportDTO> reports = createJobReports(savedRequests);
        scrapperService.scrape(savedRequests, consumer);

        return reports;
    }

    @Override
    public Map<String,Object> getReport() {
        return reportService.getReport();
    }

    @Override
    @Async
    public void stopOngoingJobs() {
        scrapperService.stopAll();
    }

    /**
     * Create job reports
     * @param requests for jobs
     * @return list of job reports
     */
    private List<JobReportDTO> createJobReports(List<JobRequestDTO> requests){
        List<JobReportDTO> jobs = requests
                .parallelStream()
                .map(this::createJobReport)
                .collect(Collectors.toList());

        return reportService.saveAll(jobs);
    }

    /**
     * Create job reports
     * @param req for job
     * @return job report
     */
    private JobReportDTO createJobReport(JobRequestDTO req){
        LOGGER.info("\nCreate report for: ({})", req);

        JobReportDTO report = new JobReportDTO();
        report.setId(req.getId());
        report.setFrn(req.getFrn());
        report.setDateTimeStarted(LocalDateTime.now().toString());
        report.setStatus(JobStatusDTO.CREATED);
        report.setPercentageComplete(ZERO_PERCENT);

        return report;
    }
}