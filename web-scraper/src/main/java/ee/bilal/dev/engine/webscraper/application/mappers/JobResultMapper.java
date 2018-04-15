package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobResultMapper extends BaseMapper<JobResultDTO, JobResult> {
    JobResultMapper INSTANCE = Mappers.getMapper(JobResultMapper.class );

    JobResult toEntity(JobResultDTO dto);
    JobResultDTO toDTO(JobResult entity);

    List<JobResultDTO> toDTOs(List<JobResult> entities);
    List<JobResult> toEntities(List<JobResultDTO> dtos);
}
