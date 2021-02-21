package com.github.jitwxs.sample.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.jitwxs.sample.shiro.entity.User;
import com.github.jitwxs.sample.shiro.mapper.UserMapper;
import com.github.jitwxs.sample.shiro.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User findByName(String name) {
        return baseMapper.findByName(name);
    }
}

