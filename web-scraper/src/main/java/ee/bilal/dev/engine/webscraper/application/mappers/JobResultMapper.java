package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobResultMapper extends BaseMapper<JobResultDTO, JobResult> {
    @Mappings({
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
    })
    @Named("toEntity")
    JobResult toEntity(JobResultDTO dto);

    @Named("toDTO")
    JobResultDTO toDTO(JobResult entity);

    @IterableMapping(qualifiedByName="toEntity")
    List<JobResult> toEntities(List<JobResultDTO> dtos);

    @IterableMapping(qualifiedByName="toDTO")
    List<JobResultDTO> toDTOs(List<JobResult> entities);
}
