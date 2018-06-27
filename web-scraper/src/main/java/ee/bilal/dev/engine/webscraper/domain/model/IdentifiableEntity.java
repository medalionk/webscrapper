package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class IdentifiableEntity extends BaseEntity{
    @Getter
    @Setter
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)", name = "id", updatable = false, nullable = false)
    protected String id;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        else if (obj == null){
            return false;
        }

        else if (!(obj instanceof IdentifiableEntity)) {
            return false;
        }

        IdentifiableEntity other = (IdentifiableEntity) obj;

        return getId().equals(other.getId());
    }
}
