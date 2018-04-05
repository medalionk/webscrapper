package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobRequestMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
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
public class JobRequestDTO implements DTO<JobRequest> {
    private String id;

    @NotEmpty
    @NonNull
    private String link;
    private long frn;
    private int levels;
    private int linksPerLevel;

    private Instant createdDate;
    private Instant lastModifiedDate;

    @Override
    public JobRequest asEntity() {
        return JobRequestMapper.INSTANCE.toEntity(this);
    }
}