package com.github.jitwxs.sample.reflection.generics;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:53
 */
public abstract class DataInfo<T> {

    abstract T getBean();

    public void print() {
        System.out.println(getBean());
    }
}
