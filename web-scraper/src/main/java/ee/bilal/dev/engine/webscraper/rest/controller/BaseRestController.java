package ee.bilal.dev.engine.webscraper.rest.controller;

import ee.bilal.dev.engine.webscraper.application.dtos.DTO;
import ee.bilal.dev.engine.webscraper.application.services.GenericService;
import ee.bilal.dev.engine.webscraper.util.ResponseUtil;
import ee.bilal.dev.engine.webscraper.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by bilal90 on 5/4/2018.
 */
public class BaseRestController<T extends DTO> extends RestControllerExceptionFilter implements Rest<T> {
    protected final GenericService<T> service;

    protected <U extends BaseRestController> BaseRestController(Class<U> tClass, GenericService<T> service) {
        super(tClass);

        this.service = service;
    }

    @Override
    public ResponseEntity<List<T>> getAll() {
        logger.info("Get all entities");

        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<T> get(String id) {
        logger.info("Get all entity with id: '{}'", id);

        ValidationUtil.validateIdentity(id);

        return ResponseUtil.wrapOrNotFound(service.findOne(id));
    }

    @Override
    public ResponseEntity<T> create(T entity) {
        logger.info("Persist entity {} in db", entity);

        ValidationUtil.validateEntity(entity);

        return new ResponseEntity<>(service.create(entity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<T> update(T entity) {
        logger.info("Update '{}' entity", entity);

        ValidationUtil.validateEntity(entity);

        return ResponseUtil.wrapOrNotFound(service.update(entity));
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        ValidationUtil.validateIdentity(id);

        logger.info("Delete entity with id: '{}'", id);
        service.delete(id);

        return ResponseEntity.ok().build();
    }
}
