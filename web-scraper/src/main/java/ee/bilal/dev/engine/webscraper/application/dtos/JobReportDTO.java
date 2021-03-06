package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class JobReportDTO implements DTO<JobReport> {
    @NotEmpty
    @NonNull
    private String id;

    @NotEmpty
    @NonNull
    private String dateTimeStarted;

    @NotEmpty
    @NonNull
    private String frn;

    private String dateTimeCompleted;
    private float percentageComplete;

    @NonNull
    private JobStatusDTO status;

    @Override
    public JobReport asEntity() {
        return JobMapper.JOB_REPORT_MAPPER.toEntity(this);
    }
}