package ee.bilal.dev.engine.webscraper.rest.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestController<T> {
    ResponseEntity<List<T>> getAll();

    ResponseEntity<T> get(String id);

    ResponseEntity<T> create(T entity);

    ResponseEntity<T> update(T entity);

    ResponseEntity<Void> delete(String id);
}
