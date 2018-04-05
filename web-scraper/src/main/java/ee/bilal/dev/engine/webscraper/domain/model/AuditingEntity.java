package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
public abstract class AuditingEntity extends IdentifiableEntity {
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    protected Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    protected Instant lastModifiedDate = Instant.now();
}
