package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobRequestMapper extends BaseMapper<JobRequestDTO, JobRequest> {
    @Mappings({
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
    })
    @Named("toEntity")
    JobRequest toEntity(JobRequestDTO dto);

    @Named("toDTO")
    JobRequestDTO toDTO(JobRequest entity);

    @IterableMapping(qualifiedByName="toEntity")
    List<JobRequest> toEntities(List<JobRequestDTO> dtos);

    @IterableMapping(qualifiedByName="toDTO")
    List<JobRequestDTO> toDTOs(List<JobRequest> entities);
}
