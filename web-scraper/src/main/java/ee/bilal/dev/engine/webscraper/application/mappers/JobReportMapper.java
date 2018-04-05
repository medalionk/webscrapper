package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.domain.model.ReportJob;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobReportMapper extends BaseMapper<JobReportDTO, ReportJob> {
    JobReportMapper INSTANCE = Mappers.getMapper(JobReportMapper.class );

    ReportJob toEntity(JobReportDTO dto);
    JobReportDTO toDTO(ReportJob entity);

    List<JobReportDTO> toDTOs(List<ReportJob> entities);
    List<ReportJob> toEntities(List<JobReportDTO> dtos);
}
