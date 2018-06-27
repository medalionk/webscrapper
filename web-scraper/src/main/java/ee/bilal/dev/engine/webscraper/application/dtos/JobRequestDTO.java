package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
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
public class JobRequestDTO implements DTO<JobRequest> {
    private String id;

    @NotEmpty
    @NonNull
    private String url;

    @NotEmpty
    @NonNull
    private String frn;

    private int maxLevel;
    private int linksPerLevel;

    private Instant createdDate;
    private Instant lastModifiedDate;

    @Override
    public JobRequest asEntity() {
        return JobMapper.JOB_REQUEST_MAPPER.toEntity(this);
    }
}