package ee.bilal.dev.engine.webscraper.application.dtos;

import ee.bilal.dev.engine.webscraper.domain.model.BaseEntity;

public interface DTO<T extends BaseEntity> {
    /**
     * Convert this DTO to Entity
     * @return converted DTO as Entity
     */
    T asEntity();
}
