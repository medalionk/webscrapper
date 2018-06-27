package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.rest.controller.RestControllerExceptionFilter;
import ee.bilal.dev.engine.webscraper.rest.validator.JobRequestValidator;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bilal90
 */

@CrossOrigin
@RestController
@RequestMapping("/api/jobs")
public class JobRestController extends RestControllerExceptionFilter {
    private final JobService jobService;
    private final JobRequestValidator requestValidator;

    @Autowired
    public JobRestController(JobService jobService, JobRequestValidator requestValidator) {
        super(JobRestController.class);

        this.jobService = jobService;
        this.requestValidator = requestValidator;
    }

    /**
     * Get all jobs
     * @return job reports
     */
    @GetMapping
    public ResponseEntity<List<JobReportDTO>> getAll() {
        logger.info("Get all jobs...");

        List<JobReportDTO> jobReports = jobService.getAllJobs();

        return ResponseEntity.ok(jobReports);
    }

    /**
     * Get job with given ID
     * @param id of jovb
     * @return job report
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobReportDTO> getJob(@PathVariable("id") String id) {
        logger.info("Get job with id: '{}'", id);

        Optional<JobReportDTO> jobReport = jobService.getJob(id);

        return ResponseUtil.wrapOrNotFound(jobReport);
    }

    /**
     * Process jos
     * @param jobRequests to process
     * @param result binding result for handling request validation
     * @return job report
     */
    @PostMapping
    public ResponseEntity<List<JobReportDTO>> process(
            @RequestBody List<JobRequestDTO> jobRequests, BindingResult result) {
        logger.info("Process jobs: '{}'", jobRequests);

        handleJobRequestValidations(jobRequests, result);
        List<JobReportDTO> jobReports = jobService.processJobs(jobRequests);

        return ResponseEntity.ok(jobReports);
    }

    /**
     * Get jobs statuses
     * @return jobs statuses
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String,Object>> getStatus() {
        logger.info("Get jobs status.");

        return ResponseEntity.ok(jobService.getStatus());
    }

    /**
     * Stop all ongoing jos
     * @return cancellation message and http code
     */
    @GetMapping("/stop-all")
    public ResponseEntity<String> stopOngoingJobs() {
        logger.info("Stop all ongoing jobs.");

        jobService.stopOngoingJobs();

        return ResponseEntity.ok("Jobs Cancellation Initiated...");
    }

    /**
     * Validate list of jobs request
     * @param requests to validate
     * @param result bind validation results
     */
    private void handleJobRequestValidations(List<JobRequestDTO> requests, BindingResult result){
        requests.forEach(x -> handleJobRequestValidation(x, result));
    }

    /**
     * Validate of job request
     * @param request to validate
     * @param result bind validation result
     */
    private void handleJobRequestValidation(JobRequestDTO request, BindingResult result){
        requestValidator.validate(request, result);

        if (result.hasErrors()){
            List<ObjectError> errors = result.getAllErrors();
            List<String> strErrors = errors.stream()
                    .map(error -> "Field: " + error.getCode() + " - " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            throw new  IllegalArgumentException(strErrors.toString());
        }
    }
}