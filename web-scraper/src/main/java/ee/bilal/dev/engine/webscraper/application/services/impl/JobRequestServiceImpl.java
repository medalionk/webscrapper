package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.mappers.JobRequestMapper;
import ee.bilal.dev.engine.webscraper.application.services.BaseGenericService;
import ee.bilal.dev.engine.webscraper.application.services.JobRequestService;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import ee.bilal.dev.engine.webscraper.domain.repository.JobRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobRequestServiceImpl extends BaseGenericService<JobRequest, JobRequestDTO> implements JobRequestService {

    @Autowired
    public JobRequestServiceImpl(JobRequestRepository repository, JobRequestMapper mapper) {
        super(JobRequestServiceImpl.class, repository, mapper);
    }

    @Override
    public JobRequestDTO create(JobRequestDTO entity) {
        if(entity == null) return null;
        return super.create(entity);
    }

    @Override
    public Optional<JobRequestDTO> update(JobRequestDTO dto) {
        return findOne(dto.getId()).map(x -> super.update(dto))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JobReport Id"));
    }

    @Override
    public List<JobRequestDTO> saveAll(List<JobRequestDTO> dtos) {
        if(dtos == null) return new ArrayList<>();
        return super.saveAll(dtos);
    }
}
