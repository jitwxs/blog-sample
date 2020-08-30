package com.github.jitwxs.demo;

import com.github.jitwxs.demo.bean.User;
import com.github.jitwxs.demo.copy.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private List<User> prepareData(int size) {
        List<User> list = new ArrayList<>(size);
        IntStream.range(0, size).forEach(e -> list.add(User.mock()));
        return list;
    }

    @GetMapping("/test/{size}")
    public String test(@PathVariable("size") Integer size) {
        List<User> data = prepareData(size);

        ApacheBeanUtilsTest apacheBeanUtilsTest = new ApacheBeanUtilsTest();
        apacheBeanUtilsTest.testCopy(data);

        SpringBeanUtilsTest springBeanUtilsTest = new SpringBeanUtilsTest();
        springBeanUtilsTest.testCopy(data);

        BeanCopierUtilsTest beanCopierUtilsTest = new BeanCopierUtilsTest();
        beanCopierUtilsTest.testCopy(data);

        BeanCopierReflectasmUtilsTest beanCopierReflectasmUtilsTest = new BeanCopierReflectasmUtilsTest();
        beanCopierReflectasmUtilsTest.testCopy(data);

        System.out.println("=====");

        NewTest newTest = new NewTest();
        newTest.testCopy(data);

        CloneTest cloneTest = new CloneTest();
        cloneTest.testCopy(data);

        System.out.println("=====");

        ToBuilderTest toBuilderTest = new ToBuilderTest();
        toBuilderTest.testCopy(data);

        NewBuilderTest newBuilderTest = new NewBuilderTest();
        newBuilderTest.testCopy(data);

        return "sss";
    }
}
