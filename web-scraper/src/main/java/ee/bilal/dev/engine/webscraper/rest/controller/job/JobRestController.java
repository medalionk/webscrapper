package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.rest.controller.RestControllerExceptionFilter;
import ee.bilal.dev.engine.webscraper.rest.validator.CollectionValidator;
import ee.bilal.dev.engine.webscraper.rest.validator.JobRequestValidator;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
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
     * @param id of job
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
     * @return job report
     */
    @ApiOperation(value = "Start Processing Jobs")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<JobReportDTO>> processJobs(@Valid @RequestBody List<JobRequestDTO> jobRequests) {
        logger.info("Process jobs: '{}'", jobRequests);

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
     * Add collection or non-collection validators to binder
     * @param binder webDataBinder
     */
    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        if(binder.getTarget() instanceof Collection){
            binder.addValidators(new CollectionValidator(requestValidator));
        }
        else {
            binder.addValidators(requestValidator);
        }
    }
}