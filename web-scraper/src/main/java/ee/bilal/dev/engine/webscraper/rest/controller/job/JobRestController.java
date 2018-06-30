package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.rest.validator.CollectionValidator;
import ee.bilal.dev.engine.webscraper.rest.validator.JobRequestValidator;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by bilal90
 */

@CrossOrigin
@RestController
@RequestMapping("/api/jobs")
public class JobRestController {
    private final Logger logger;
    private final JobService jobService;
    private final JobRequestValidator requestValidator;

    @Autowired
    public JobRestController(JobService jobService, JobRequestValidator requestValidator) {
        this.logger = LoggerFactory.getLogger(JobRestController.class);
        this.jobService = jobService;
        this.requestValidator = requestValidator;
    }

    /**
     * Get all jobs
     * @return job reports
     */
    @ApiOperation(value = "Get all Jobs")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiOperation(value = "Get one Job")
    @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobReportDTO>> processJobs(@Valid @RequestBody List<JobRequestDTO> jobRequests) {
        logger.info("Process jobs: '{}'", jobRequests);

        List<JobReportDTO> jobReports = jobService.processJobs(jobRequests);

        return ResponseEntity.ok(jobReports);
    }

    /**
     * Get jobs statuses
     * @return jobs statuses
     */
    @ApiOperation(value = "Get report for all Jobs submitted")
    @GetMapping(path = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getStatus() {
        logger.info("Get jobs report.");

        return ResponseEntity.ok(jobService.getStatus());
    }

    /**
     * Stop all ongoing jos
     * @return cancellation message and http code
     */
    @ApiOperation(value = "Stop all ongoing Jobs")
    @GetMapping(path = "/stop-all", produces = MediaType.APPLICATION_JSON_VALUE)
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