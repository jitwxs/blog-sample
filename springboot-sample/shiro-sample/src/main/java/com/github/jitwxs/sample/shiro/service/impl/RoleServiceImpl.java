package com.github.jitwxs.sample.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.jitwxs.sample.shiro.entity.Role;
import com.github.jitwxs.sample.shiro.mapper.RoleMapper;
import com.github.jitwxs.sample.shiro.service.IRoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
