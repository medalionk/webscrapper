package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.DTO;
import ee.bilal.dev.engine.webscraper.application.mappers.BaseMapper;
import ee.bilal.dev.engine.webscraper.domain.model.BaseEntity;
import ee.bilal.dev.engine.webscraper.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bilal90 on 5/4/2018.
 */
@Service
public abstract class BaseGenericService<U extends BaseEntity, T extends DTO<U>> implements GenericService<T> {
    protected final Logger LOGGER;
    protected final JpaRepository<U, String> repository;
    protected final BaseMapper<T, U> mapper;

    protected <S extends BaseGenericService>BaseGenericService(
            Class<S> tClass, JpaRepository<U, String> repository, BaseMapper<T, U> mapper) {
        this.LOGGER = LoggerFactory.getLogger(tClass);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public T create(T entity) {
        LOGGER.info("Create entity: '{}'", entity);
        ValidationUtil.validateEntity(entity);
        U savedEntity = repository.saveAndFlush(entity.asEntity());
        return mapper.toDTO(savedEntity);
    }

    @Override
    public Optional<T> update(T entity){
        LOGGER.info("Update entity: '{}'", entity);
        ValidationUtil.validateEntity(entity);
        return Optional.ofNullable(repository.saveAndFlush(entity.asEntity()))
                .map(mapper::toDTO);
    }

    @Override
    public Optional<T> findOne(String id) {
        LOGGER.info("Fetch one entity with id: '{}'", id);
        ValidationUtil.validateIdentity(id);
        U entity = repository.getOne(id);
        return Optional.ofNullable(entity).map(mapper::toDTO);
    }

    @Override
    public List<T> findAll(){
        LOGGER.info("Fetch all entities found");
        List<U> results = repository.findAll();
        if(results.isEmpty()) {
            LOGGER.info("No entities found");
            return new ArrayList<>();
        }

        return mapper.toDTOs(results);
    }

    @Override
    public void delete(String id){
        LOGGER.info("Delete entity with id: '{}'", id);
        ValidationUtil.validateIdentity(id);

        Optional<T> t = findOne(id);
        if(!t.isPresent()){
            throw new IllegalArgumentException("Invalid Id");
        }
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<T> saveAll(List<T> dtos) {
        ValidationUtil.validateEntity(dtos);
        List<U> entities = dtos.stream().map(DTO::asEntity).collect(Collectors.toList());
        List<U> savedEntities = repository.saveAll(entities);
        return mapper.toDTOs(savedEntities);
    }
}
