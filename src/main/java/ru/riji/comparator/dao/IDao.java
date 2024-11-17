package ru.riji.comparator.dao;

import ru.riji.comparator.form.IForm;

import java.util.List;

public interface IDao<T, F extends IForm> {
    List<T> getAll();
    T getById(int id);
    int add(F f);
    void update(F f);
    void update(T t);
    void delete(int id);
}
