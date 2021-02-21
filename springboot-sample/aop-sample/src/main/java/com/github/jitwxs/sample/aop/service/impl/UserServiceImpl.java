package com.github.jitwxs.sample.aop.service.impl;

import com.github.jitwxs.sample.aop.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void delete(String name) {
        System.out.println("delete：" + name);
    }
}