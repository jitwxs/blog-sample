package com.github.jitwxs.sample.reflection.bean;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:02
 */
@Data
@SuperBuilder
public class User implements Serializable {
    private long id;

    private String username;

    private boolean female;

    public static User random() {
        return User.builder()
                .id(RandomUtils.nextLong())
                .username(RandomStringUtils.random(5))
                .female(RandomUtils.nextBoolean())
                .build();
    }
}
