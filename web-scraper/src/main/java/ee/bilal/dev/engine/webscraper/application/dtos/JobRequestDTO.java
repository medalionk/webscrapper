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

    public static JobRequestDTO of(String url, String frn, int maxLevel, int linksPerLevel){
        JobRequestDTO request = new JobRequestDTO();
        request.setUrl(url);
        request.setFrn(frn);
        request.setMaxLevel(maxLevel);
        request.setLinksPerLevel(linksPerLevel);

        return request;
    }

    @Override
    public JobRequest asEntity() {
        return JobRequestMapper.INSTANCE.toEntity(this);
    }
}