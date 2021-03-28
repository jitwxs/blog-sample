package com.github.jitwxs.sample.reflection.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:43
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private Integer age;
}
