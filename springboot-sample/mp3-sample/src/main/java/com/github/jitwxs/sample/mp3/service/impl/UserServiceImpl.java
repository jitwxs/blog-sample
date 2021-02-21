package com.github.jitwxs.sample.mp3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.jitwxs.sample.mp3.enity.User;
import com.github.jitwxs.sample.mp3.mapper.UserMapper;
import com.github.jitwxs.sample.mp3.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}