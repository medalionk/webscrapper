package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.DTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GenericService<T extends DTO> {
    T create(T entity);

    Optional<T> update(T entity);

    Optional<T> findOne(String id);

    List<T> findAll();

    void delete(String id);

    long count();

    List<T> saveAll(List<T> entities);
}
