package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Entity
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = true)
public class ReportJob extends AuditingEntity {
    @NotEmpty
    @NonNull
    @Column(name = "date_time_started", nullable = false)
    private String dateTimeStarted;

    @Column(name = "date_time_completed")
    private String dateTimeCompleted;

    @Column(name = "percentage_complete")
    private float percentageComplete;

    @Column(name = "frn", nullable = false)
    private long frn;

    @NotEmpty
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "line_category", columnDefinition="char(32) default 'STARTED'")
    private JobStatus status;
}
