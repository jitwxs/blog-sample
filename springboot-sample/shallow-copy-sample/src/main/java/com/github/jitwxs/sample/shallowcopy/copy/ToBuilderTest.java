package com.github.jitwxs.sample.shallowcopy.copy;


import com.github.jitwxs.sample.shallowcopy.bean.User;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class ToBuilderTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        System.out.println(source);
        System.out.println(source.toBuilder().build());
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            User target = source.toBuilder().build();
        }
    }

    @Override
    String name() {
        return "Lombok toBuilder";
    }
}
