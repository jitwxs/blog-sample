package com.github.jitwxs.sample.reflection.generics;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:59
 */
public interface IDataService<T> {
    void print(T bean);
}
