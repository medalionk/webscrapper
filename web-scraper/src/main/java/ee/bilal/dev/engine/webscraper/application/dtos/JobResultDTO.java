package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.Mappers;
import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(force = true)
public class JobResultDTO implements DTO<JobResult> {
    private String id;

    @NotEmpty
    @NonNull
    private String jobId;

    @NotEmpty
    @NonNull
    private String frn;

    @NotEmpty
    @NonNull
    private String url;

    private String text;

    private Instant createdDate;
    private Instant lastModifiedDate;

    public static JobResultDTO of(String jobId, String frn, String url, String text){
        JobResultDTO result = new JobResultDTO();
        result.setJobId(jobId);
        result.setFrn(frn);
        result.setUrl(url);
        result.setText(text);

        return result;
    }

    @Override
    public JobResult asEntity() {
        return Mappers.JOB_RESULT_MAPPER.toEntity(this);
    }
}