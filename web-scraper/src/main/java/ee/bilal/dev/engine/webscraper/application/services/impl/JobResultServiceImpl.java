package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.mappers.JobResultMapper;
import ee.bilal.dev.engine.webscraper.application.services.BaseGenericService;
import ee.bilal.dev.engine.webscraper.application.services.JobResultService;
import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import ee.bilal.dev.engine.webscraper.domain.repository.JobResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobResultServiceImpl extends BaseGenericService<JobResult, JobResultDTO> implements JobResultService {

    @Autowired
    public JobResultServiceImpl(JobResultRepository repository, JobResultMapper mapper) {
        super(JobResultServiceImpl.class, repository, mapper);
    }

    @Override
    public JobResultDTO create(JobResultDTO entity) {
        if(entity == null) return null;
        return super.create(entity);
    }

    @Override
    public Optional<JobResultDTO> update(JobResultDTO dto) {
        return findOne(dto.getId()).map(x -> super.update(dto))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));
    }

    @Override
    public List<JobResultDTO> saveAll(List<JobResultDTO> dtos) {
        if(dtos == null) return new ArrayList<>();
        return super.saveAll(dtos);
    }
}
