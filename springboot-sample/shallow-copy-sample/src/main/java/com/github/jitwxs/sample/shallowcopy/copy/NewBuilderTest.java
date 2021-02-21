package com.github.jitwxs.sample.shallowcopy.copy;



import com.github.jitwxs.sample.shallowcopy.bean.User;

import java.util.List;

/**
 * @author jitwxs
 * @date 2020年08月29日 23:02
 */
public class NewBuilderTest extends BaseCopyTest {

    @Override
    void warnUp() {
        User source = prepareOne();
        System.out.println(source);
        System.out.println(this.copy(source));
    }

    @Override
    void copyLogic(List<User> data) {
        for(User source : data) {
            User target = this.copy(source);
        }
    }

    private User copy(User source) {
        return User.builder()
                .id(source.getId())
                .age(source.getAge())
                .name(source.getName())
                .isMale(source.isMale())
                .school(source.getSchool())
                .createDate(source.getCreateDate())
                .build();
    }

    @Override
    String name() {
        return "Lombok newBuilder";
    }
}
