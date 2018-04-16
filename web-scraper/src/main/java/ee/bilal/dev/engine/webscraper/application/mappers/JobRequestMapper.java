package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobRequestMapper extends BaseMapper<JobRequestDTO, JobRequest> {
    JobRequestMapper INSTANCE = Mappers.getMapper(JobRequestMapper.class );

    @Mappings({
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
    })
    JobRequest toEntity(JobRequestDTO dto);
    JobRequestDTO toDTO(JobRequest entity);

    List<JobRequestDTO> toDTOs(List<JobRequest> entities);

    @IterableMapping(qualifiedByName="toEntity")
    List<JobRequest> toEntities(List<JobRequestDTO> dtos);
}
