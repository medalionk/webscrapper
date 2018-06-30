package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Data
@NoArgsConstructor(force = true)
public class JobRequestDTO implements DTO<JobRequest> {
    @NotNull(groups = Existing.class)
    @Null(groups = New.class)
    private String id;

    @NotEmpty
    @NotBlank
    @NotNull(
            message = "Url is required",
            groups = {Existing.class, New.class}
    )
    private String url;

    @NotEmpty
    @NonNull
    private String frn;

    @Min(1)
    private int maxLevel;

    @Min(1)
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
        return JobMapper.JOB_REQUEST_MAPPER.toEntity(this);
    }

    public interface Existing {
    }

    public interface New {
    }
}