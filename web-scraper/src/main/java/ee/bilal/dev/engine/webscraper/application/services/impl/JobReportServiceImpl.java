package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.application.mappers.JobReportMapper;
import ee.bilal.dev.engine.webscraper.application.services.BaseGenericService;
import ee.bilal.dev.engine.webscraper.application.services.JobReportService;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import ee.bilal.dev.engine.webscraper.domain.repository.JobReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class JobReportServiceImpl extends BaseGenericService<JobReport, JobReportDTO> implements JobReportService {
    private static final double HUNDRED_PERCENT = 100.0;

    @Autowired
    public JobReportServiceImpl(JobReportRepository repository, JobReportMapper mapper) {
        super(JobReportServiceImpl.class, repository, mapper);
    }

    @Override
    public JobReportDTO create(JobReportDTO entity) {
        if(entity == null) return null;
        return super.create(entity);
    }

    @Override
    public Optional<JobReportDTO> update(JobReportDTO dto) {
        return findOne(dto.getId()).map(x -> super.update(dto))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));
    }

    @Override
    public List<JobReportDTO> saveAll(List<JobReportDTO> dtos) {
        if(dtos == null) return new ArrayList<>();
        return super.saveAll(dtos);
    }

    @Override
    @Transactional
    public Optional<JobReportDTO> updateProgress(String id, float progress, JobStatusDTO status) {
        return updateReport(id, x -> {
            x.setPercentageComplete(x.getPercentageComplete() + progress);
            x.setStatus(status);
        });
    }

    @Override
    @Transactional
    public Optional<JobReportDTO> markCompleted(String id) {
        return updateReport(id, x -> {
            x.setPercentageComplete(100);
            x.setStatus(JobStatusDTO.COMPLETED);
            x.setDateTimeCompleted(LocalDateTime.now().toString());
        });
    }

    @Override
    public Map<String,Object> getStatus() {
        Map<String,Object> status = new HashMap<>();
        List<JobReportDTO> reports = findAll();
        long totalJobs = reports.size();
        long totalCompleted = statusCount(reports, JobStatusDTO.COMPLETED);
        long totalOngoing = statusCount(reports, JobStatusDTO.ONGOING);
        long totalCanceled = statusCount(reports, JobStatusDTO.CANCELED);
        double percentCompleted = getPercentCompleted(reports);

        status.put("totalJobs", totalJobs);
        status.put("totalCompleted", totalCompleted);
        status.put("totalOngoing", totalOngoing);
        status.put("totalCanceled", totalCanceled);
        status.put("percentCompleted", String.format("%.2f", percentCompleted));

        return status;
    }

    private double getPercentCompleted(List<JobReportDTO> reports){
        double total = reports.size() * HUNDRED_PERCENT;
        double progress = reports.stream().mapToDouble(JobReportDTO::getPercentageComplete).sum();

        return (progress / total) * HUNDRED_PERCENT;
    }

    private long statusCount(List<JobReportDTO> reports, JobStatusDTO status){
        return reports.stream().filter(x -> x.getStatus() == status).count();
    }

    @Transactional
    public Optional<JobReportDTO> updateReport(String id, Consumer<JobReportDTO> consumer) {
        return findOne(id).map(x -> {
            consumer.accept(x);
            return super.update(x);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));
    }
}
