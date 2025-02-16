package cz.cvut.fit.tjv.easyvps.service;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public interface CrudServiceInterface<T, ID> {

    T create(T e) throws IllegalArgumentException;

    Optional<T> readById(ID id);

    Iterable<T> readAll();

    void update(ID id, T e) throws EntityNotFoundException;

    void deleteById(ID id) throws EntityNotFoundException;
}
