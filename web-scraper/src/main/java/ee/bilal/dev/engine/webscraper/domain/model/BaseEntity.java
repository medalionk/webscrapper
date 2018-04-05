package ee.bilal.dev.engine.webscraper.domain.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

}