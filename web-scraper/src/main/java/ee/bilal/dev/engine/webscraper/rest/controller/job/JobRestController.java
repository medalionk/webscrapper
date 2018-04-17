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

    @GetMapping
    public ResponseEntity<List<JobReportDTO>> getAll() {
        LOGGER.info("Get all jobs...");
        List<JobReportDTO> jobReports = jobService.getAllJobs();
        return ResponseEntity.ok(jobReports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobReportDTO> getJob(@PathVariable("id") String id) {
        LOGGER.info("Get job with id: '{}'", id);
        Optional<JobReportDTO> jobReport = jobService.getJob(id);
        return ResponseUtil.wrapOrNotFound(jobReport);
    }

    @PostMapping
    public ResponseEntity<List<JobReportDTO>> process(
            @RequestBody List<JobRequestDTO> jobRequests, BindingResult result) {
        LOGGER.info("Process jobs: '{}'", jobRequests);

        handleJobRequestValidations(jobRequests, result);
        List<JobReportDTO> jobReports = jobService.processJobs(jobRequests);
        return ResponseEntity.ok(jobReports);
    }

    private void handleJobRequestValidations(List<JobRequestDTO> requests, BindingResult result){
        requests.forEach(x -> handleJobRequestValidation(x, result));
    }

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