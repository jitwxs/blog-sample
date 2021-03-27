package com.github.jitwxs.sample.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Dynamic Schedule Application
 * @author jitwxs
 * @date 2021年03月27日 16:27
 */
@EnableScheduling
@SpringBootApplication
public class DSApplication {
    public static void main(String[] args) {
        SpringApplication.run(DSApplication.class, args);
    }
}
