package com.ct.sprintnba_demo01.base.adapter;

import java.util.List;

/**
 * Created by ct on 2017/1/12.
 */

public interface DataHelper<T>{
    boolean addAll(List<T> list);

    boolean addAll(int position, List<T> list);

    void add(T data);

    void add(int position, T data);

    void clear();

    boolean contains(T data);

    T getData(int index);

    void modify(T oldData, T newData);

    void modify(int index, T newData);

    boolean remove(T data);

    T remove(int index);
}
