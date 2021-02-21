package com.github.jitwxs.sample.shallowcopy.copy;

import com.github.jitwxs.sample.shallowcopy.bean.User;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class ApacheBeanUtilsTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        try {
            User target = new User();
            System.out.println(source);
            BeanUtils.copyProperties(target, source);
            System.out.println(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            try {
                BeanUtils.copyProperties(new User(), source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    String name() {
        return "Apache BeanUtils";
    }
}
