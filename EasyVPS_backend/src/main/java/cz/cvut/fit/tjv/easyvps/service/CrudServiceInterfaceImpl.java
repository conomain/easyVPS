package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.EntityWithId;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


@Transactional
public abstract class CrudServiceInterfaceImpl<T extends EntityWithId<ID>, ID> implements CrudServiceInterface<T, ID> {

    protected abstract CrudRepository<T, ID> getRepository();

    @Override
    public T create(T e) throws IllegalArgumentException {
        if (e.getId() != null && getRepository().existsById(e.getId())) {
            throw new IllegalArgumentException("Entity with id " + e.getId() + " already exists.");
        }
        return getRepository().save(e);
    }

    @Override
    public T readById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id " + id + " does not exist."));
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(ID id, T e) throws EntityNotFoundException {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " does not exist.");
        }
        getRepository().save(e);
    }

    @Override
    public void deleteById(ID id) throws EntityNotFoundException {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " does not exist.");
        }
        getRepository().deleteById(id);
    }
}
