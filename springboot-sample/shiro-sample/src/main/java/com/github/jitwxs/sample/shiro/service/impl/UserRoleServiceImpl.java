package com.github.jitwxs.sample.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.jitwxs.sample.shiro.entity.UserRole;
import com.github.jitwxs.sample.shiro.mapper.UserRoleMapper;
import com.github.jitwxs.sample.shiro.service.IUserRoleService;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
