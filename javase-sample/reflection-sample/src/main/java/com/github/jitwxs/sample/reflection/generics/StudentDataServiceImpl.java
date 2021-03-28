package com.github.jitwxs.sample.reflection.generics;

import com.github.jitwxs.sample.reflection.bean.Student;

/**
 * @author jitwxs
 * @date 2021年03月28日 19:59
 */
public class StudentDataServiceImpl implements IDataService<Student> {
    private volatile int count = 0;

    private final static String CLASS_NAME = "StudentDataServiceImpl";

    @Override
    public void print(Student bean) {
        System.out.println("Execute StudentDataServiceImpl#print");
    }
}
