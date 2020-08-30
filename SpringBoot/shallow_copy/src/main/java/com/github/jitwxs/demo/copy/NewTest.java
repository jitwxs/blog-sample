package com.github.jitwxs.demo.copy;

import com.github.jitwxs.demo.bean.User;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class NewTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        try {
            User target = new User();
            System.out.println(source);
            target.setId(source.getId());
            target.setAge(source.getAge());
            target.setName(source.getName());
            target.setMale(source.isMale());
            target.setSchool(source.getSchool());
            target.setCreateDate(source.getCreateDate());
            System.out.println(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            User target = new User();
            target.setId(source.getId());
            target.setAge(source.getAge());
            target.setName(source.getName());
            target.setMale(source.isMale());
            target.setSchool(source.getSchool());
            target.setCreateDate(source.getCreateDate());
        }
    }

    @Override
    String name() {
        return "Java New";
    }
}
