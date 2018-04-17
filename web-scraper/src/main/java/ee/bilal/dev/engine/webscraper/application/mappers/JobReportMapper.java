package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobReportMapper extends BaseMapper<JobReportDTO, JobReport> {
    JobReport toEntity(JobReportDTO dto);
    JobReportDTO toDTO(JobReport entity);

    List<JobReportDTO> toDTOs(List<JobReport> entities);
    List<JobReport> toEntities(List<JobReportDTO> dtos);
}
