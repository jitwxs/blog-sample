package com.github.jitwxs.sample.reflection.generics;

import com.github.jitwxs.sample.reflection.bean.User;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:54
 */
public class UserDataInfo extends DataInfo<User> {
    @Override
    User getBean() {
        return User.random();
    }
}
