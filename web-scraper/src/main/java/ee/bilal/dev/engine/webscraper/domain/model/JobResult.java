package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = true)
public class JobResult extends AuditingEntity {
    @NotEmpty
    @NonNull
    @Column(name = "job_id", nullable = false)
    private String jobId;

    @NotEmpty
    @NonNull
    @Column(name = "frn", nullable = false)
    private String frn;

    @NotEmpty
    @NonNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotEmpty
    @NonNull
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
}
