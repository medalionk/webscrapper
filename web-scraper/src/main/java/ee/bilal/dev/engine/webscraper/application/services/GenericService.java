package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.DTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GenericService<T extends DTO> {
    /**
     * Create entity
     * @param entity to create
     * @return created entity
     */
    T create(T entity);

    /**
     * Create entity
     * @param entity to update
     * @return updated entity
     */
    Optional<T> update(T entity);

    /**
     * Find one entity with given ID
     * @param id of enity to get
     * @return found entity
     */
    Optional<T> findOne(String id);

    /**
     * Get all entities
     * @return list of entities
     */
    List<T> findAll();

    /**
     * Delete entity with given ID
     * @param id of entity to delete
     */
    void delete(String id);

    /**
     * Get entities count
     * @return number of entities
     */
    long count();

    /**
     * Save all entities
     * @param entities to save
     * @return list of saved entities
     */
    List<T> saveAll(List<T> entities);
}
