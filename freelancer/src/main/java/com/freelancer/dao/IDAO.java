package com.freelancer.dao;

import java.util.List;

public interface IDAO<T> {
    void save(T entity);
    void update(T entity);
    void delete(String id);
    T get(String id);
    List<T> getAll();
}
