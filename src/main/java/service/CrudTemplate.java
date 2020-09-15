package service;

import domain.BaseEntity;
import dto.BaseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;

public abstract class CrudTemplate<T extends BaseEntity, V extends JpaRepository<T, Long>> {
    V repository;

    public T create(T entity) {
        return repository.save(entity);
    }

    public T findById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        T foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new NullPointerException();
        return foundEntity;
    }

    public Long delete(Long id) {
        T entity = repository.findById(id).orElse(null);
        if (Objects.isNull(entity))
            throw new NullPointerException();
        repository.delete(entity);
        return id;
    }

    public List<T> findAll() {
        return repository.findAll();
    }
}
