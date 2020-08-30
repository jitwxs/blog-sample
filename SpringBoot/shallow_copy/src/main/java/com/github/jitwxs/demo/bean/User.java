package com.github.jitwxs.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User implements Cloneable {
    private long id;

    private int age;

    private String name;

    private boolean isMale;

    private School school;

    private Date createDate;

    public static User mock() {
        return User.builder()
                .id(RandomUtils.nextLong())
                .age(RandomUtils.nextInt())
                .name(RandomStringUtils.randomAlphanumeric(5))
                .isMale(RandomUtils.nextBoolean())
                .school(new School(RandomStringUtils.randomAlphanumeric(5), RandomUtils.nextInt()))
                .createDate(new Date())
                .build();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
