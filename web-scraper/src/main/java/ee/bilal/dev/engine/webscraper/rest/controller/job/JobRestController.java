package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by bilal90
 */

@CrossOrigin
@RestController
@RequestMapping("/api/jobs")
public class JobRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRestController.class);
    private final JobService jobService;

    @Autowired
    public JobRestController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobReportDTO>> getAll(HttpServletRequest request) {
        LOGGER.info("Get all jobs...");
        List<JobReportDTO> jobReports = jobService.getAllJobs();
        return ResponseEntity.ok(jobReports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobReportDTO> getJob(HttpServletRequest request, @PathVariable("id") String id) {
        LOGGER.info("Get job with id: '{}'", id);
        Optional<JobReportDTO> jobReport = jobService.getJob(id);
        return ResponseUtil.wrapOrNotFound(jobReport);
    }

    @PostMapping
    public ResponseEntity<List<JobReportDTO>> process(
            HttpServletRequest request, @RequestBody List<JobRequestDTO> jobRequests) {
        LOGGER.info("Process jobs: '{}'", jobRequests);
        List<JobReportDTO> jobReports = jobService.processJobs(jobRequests);
        return ResponseEntity.ok(jobReports);
    }
}