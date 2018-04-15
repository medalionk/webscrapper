package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.application.mappers.JobResultMapper;
import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import lombok.*;

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
    private String frn;

    @NotEmpty
    @NonNull
    private String url;

    private String text;

    private Instant createdDate;
    private Instant lastModifiedDate;

    public static JobResultDTO of(String url, String text){
        JobResultDTO result = new JobResultDTO();
        result.setUrl(url);
        result.setText(text);

        return result;
    }

    @Override
    public JobResult asEntity() {
        return JobResultMapper.INSTANCE.toEntity(this);
    }
}