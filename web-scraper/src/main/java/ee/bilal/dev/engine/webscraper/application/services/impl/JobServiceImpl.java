package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.domain.repository.JobRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRequestRepository requestRepository;

    @Autowired
    public JobServiceImpl(JobRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<JobReportDTO> getAllJobs() {
        return new ArrayList<>();
    }

    @Override
    public JobReportDTO getJob(String jobId) {
        return null;
    }

    @Override
    public List<JobRequestDTO> processJobs(List<JobRequestDTO> requests) {
        return new ArrayList<>();
    }
}
