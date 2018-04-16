package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.mappers.JobReportMapper;
import ee.bilal.dev.engine.webscraper.application.services.BaseGenericService;
import ee.bilal.dev.engine.webscraper.application.services.JobReportService;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import ee.bilal.dev.engine.webscraper.domain.repository.JobReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobReportServiceImpl extends BaseGenericService<JobReport, JobReportDTO> implements JobReportService {

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
    public JobReportDTO updateProgress(String id, float progress) {
        //JobReport report = repository.getOne(id);
        return Optional.ofNullable(repository.getOne(id)).map(x -> {
            x.setPercentageComplete(x.getPercentageComplete() + progress);
            return JobReportMapper.INSTANCE.toDTO(repository.saveAndFlush(x));
        }).orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));

        /*return repository.getOne(id).map(x -> {
            x.setPercentageComplete(x.getPercentageComplete() + progress);
            return super.update(x);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));*/
    }
}
