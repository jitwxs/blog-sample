package com.github.jitwxs.sample.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.jitwxs.sample.shiro.entity.Permission;
import com.github.jitwxs.sample.shiro.mapper.PermissionMapper;
import com.github.jitwxs.sample.shiro.service.IPermissionService;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
