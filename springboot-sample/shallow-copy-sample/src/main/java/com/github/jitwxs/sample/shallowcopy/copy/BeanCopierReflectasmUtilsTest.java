package com.github.jitwxs.sample.shallowcopy.copy;



import com.github.jitwxs.sample.shallowcopy.bean.User;
import com.github.jitwxs.sample.shallowcopy.utils.BeanCopierReflectasmUtils;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class BeanCopierReflectasmUtilsTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        try {
            System.out.println(source);
            System.out.println(BeanCopierReflectasmUtils.copyProperties(source, User.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            User target = BeanCopierReflectasmUtils.copyProperties(source, User.class);
        }
    }

    @Override
    String name() {
        return "Spring BeanCopier Reflectasm";
    }
}
