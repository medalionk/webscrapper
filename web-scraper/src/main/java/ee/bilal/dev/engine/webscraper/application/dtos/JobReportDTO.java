package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobReportMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class JobReportDTO implements DTO<JobReport> {
    private String id;

    @NotEmpty
    @NonNull
    private String dateTimeStarted;

    @NotEmpty
    @NonNull
    private String frn;

    private String dateTimeCompleted;
    private float percentageComplete;

    @NotEmpty
    @NonNull
    private JobStatusDTO status;

    private Instant createdDate;
    private Instant lastModifiedDate;

    @Override
    public JobReport asEntity() {
        return JobReportMapper.INSTANCE.toEntity(this);
    }
}