package com.github.jitwxs.demo.copy;


import com.github.jitwxs.demo.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:03
 */
public abstract class BaseCopyTest {
    public List<User> prepareData(int size) {
        List<User> list = new ArrayList<>(size);
        IntStream.range(0, size).forEach(e -> list.add(User.mock()));
        return list;
    }

    public User prepareOne() {
        return User.mock();
    }

    public void testCopy(List<User> data) {
        warnUp();

        long startTime = System.currentTimeMillis();

        copyLogic(data);

        System.out.println(name() + ": " + (System.currentTimeMillis() - startTime));
    }

    abstract void warnUp();

    abstract void copyLogic(List<User> data);

    abstract String name();
}
