package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobReportMapper;
import ee.bilal.dev.engine.webscraper.domain.model.ReportJob;
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
public class JobReportDTO implements DTO<ReportJob> {
    private String id;

    @NotEmpty
    @NonNull
    private String dateTimeStarted;
    private String dateTimeCompleted;
    private float percentageComplete;
    private long frn;

    @NotEmpty
    @NonNull
    private JobStatusDTO status;

    private Instant createdDate;
    private Instant lastModifiedDate;

    @Override
    public ReportJob asEntity() {
        return JobReportMapper.INSTANCE.toEntity(this);
    }
}