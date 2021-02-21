package com.github.jitwxs.sample.shallowcopy.copy;



import java.util.List;

import com.github.jitwxs.sample.shallowcopy.bean.User;
import org.springframework.beans.BeanUtils;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class SpringBeanUtilsTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        User target = new User();
        System.out.println(source);
        BeanUtils.copyProperties(source, target);
        System.out.println(target);
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            BeanUtils.copyProperties(source, new User());
        }
    }

    @Override
    String name() {
        return "Spring BeanUtils";
    }
}
