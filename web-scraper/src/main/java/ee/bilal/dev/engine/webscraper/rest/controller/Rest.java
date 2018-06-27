package ee.bilal.dev.engine.webscraper.rest.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface Rest<T> {
    /**
     * Get all Entities
     * @return return all Entities with response http code
     */
    ResponseEntity<List<T>> getAll();

    /**
     * Get one entity
     * @param id of entity to get
     * @return entity with response http code
     */
    ResponseEntity<T> get(String id);

    /**
     * Create entity
     * @param entity to create
     * @return created entity with response http code
     */
    ResponseEntity<T> create(T entity);

    /**
     * Update entity
     * @param entity to update
     * @return updated entity with response http code
     */
    ResponseEntity<T> update(T entity);

    /**
     * Delete entity
     * @param id of entity to delete
     * @return response http code
     */
    ResponseEntity<Void> delete(String id);
}
