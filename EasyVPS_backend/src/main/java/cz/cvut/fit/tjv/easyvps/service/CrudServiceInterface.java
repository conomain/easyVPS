package cz.cvut.fit.tjv.easyvps.service;

import java.util.Optional;

public interface CrudServiceInterface<T, ID> {

    T create(T e) throws IllegalArgumentException;

    Optional<T> readById(ID id);

    Iterable<T> readAll();

    void update(ID id, T e) throws IllegalArgumentException;

    void deleteById(ID id) throws IllegalArgumentException;
}
