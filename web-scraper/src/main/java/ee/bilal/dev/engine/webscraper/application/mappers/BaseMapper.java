package ee.bilal.dev.engine.webscraper.application.mappers;

import ee.bilal.dev.engine.webscraper.domain.model.BaseEntity;

import java.util.List;

public interface BaseMapper<T, U extends BaseEntity> {
    U toEntity(T dto);
    T toDTO(U entity);

    List<T> toDTOs(List<U> entities);
    List<U> toEntities(List<T> dtos);
}
