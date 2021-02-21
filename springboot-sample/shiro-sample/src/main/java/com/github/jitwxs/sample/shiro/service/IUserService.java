package com.github.jitwxs.sample.shiro.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.jitwxs.sample.shiro.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-20
 */
public interface IUserService extends IService<User> {
    User findByName(String name);
}
