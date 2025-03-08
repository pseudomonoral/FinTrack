package com.financeTracker.dao;

import java.util.Optional;

public interface MainDao <I,T>{

    Optional<T> findById(I id);

    T save(T entity);

}
