package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = true)
public class JobReport extends BaseEntity {
    @Id
    @Column(columnDefinition = "CHAR(32)", name = "id", updatable = false, nullable = false)
    protected String id;

    @NotEmpty
    @NonNull
    @Column(name = "date_time_started", nullable = false)
    private String dateTimeStarted;

    @NotEmpty
    @NonNull
    @Column(name = "frn", nullable = false)
    private String frn;

    @Column(name = "date_time_completed")
    private String dateTimeCompleted;

    @Column(name = "percentage_complete")
    private float percentageComplete;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "line_category", columnDefinition="char(32) default 'STARTED'")
    private JobStatus status;
}
