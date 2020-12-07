package dao;

import java.util.ArrayList;
import java.util.List;

public interface EntityDao<T> {

//    T getByLogin(int login, boolean full);

    T getById(int id, boolean full);

    List<T> getAll();

    boolean create(T entity);

    boolean update(T entity);

    boolean remove(T entity);

}
