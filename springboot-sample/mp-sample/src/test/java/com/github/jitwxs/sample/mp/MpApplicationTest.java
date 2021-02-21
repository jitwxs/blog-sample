package com.github.jitwxs.sample.mp;

import com.github.jitwxs.sample.mp.enity.User;
import com.github.jitwxs.sample.mp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpApplicationTest {
    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        List<User> users = userService.selectList(null);
        System.out.println(users);
    }

}