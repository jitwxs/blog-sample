package com.github.jitwxs.sample.mp.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.jitwxs.sample.mp.enity.User;
import com.github.jitwxs.sample.mp.mapper.UserMapper;
import com.github.jitwxs.sample.mp.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}