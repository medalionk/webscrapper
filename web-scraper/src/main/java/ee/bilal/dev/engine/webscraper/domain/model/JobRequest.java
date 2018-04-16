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
public class JobRequest extends AuditingEntity {
    @NotEmpty
    @NonNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotEmpty
    @NonNull
    @Column(name = "frn", nullable = false)
    private String frn;

    @Column(name = "max_level")
    private int maxLevel;

    @Column(name = "links_per_level")
    private int linksPerLevel;
}
