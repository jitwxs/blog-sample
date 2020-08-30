package com.github.jitwxs.demo.copy;


import com.github.jitwxs.demo.bean.User;
import com.github.jitwxs.demo.utils.BeanCopierUtils;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class BeanCopierUtilsTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        User target = new User();
        System.out.println(source);
        BeanCopierUtils.copyProperties(source, target);
        System.out.println(target);
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            BeanCopierUtils.copyProperties(source, new User());
        }
    }

    @Override
    String name() {
        return "Spring BeanCopier";
    }
}
