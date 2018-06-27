package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobReportMapper extends BaseMapper<JobReportDTO, JobReport> {
    @Named("toEntity")
    JobReport toEntity(JobReportDTO dto);

    @Named("toDTO")
    JobReportDTO toDTO(JobReport entity);

    @IterableMapping(qualifiedByName="toEntity")
    List<JobReport> toEntities(List<JobReportDTO> dtos);

    @IterableMapping(qualifiedByName="toDTO")
    List<JobReportDTO> toDTOs(List<JobReport> entities);
}
