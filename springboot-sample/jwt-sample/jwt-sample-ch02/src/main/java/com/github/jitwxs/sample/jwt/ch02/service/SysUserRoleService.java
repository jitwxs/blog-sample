package com.github.jitwxs.sample.jwt.ch02.service;

import com.github.jitwxs.sample.jwt.ch02.entity.SysUserRole;
import com.github.jitwxs.sample.jwt.ch02.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jitwxs
 * @date 2018/3/30 1:29
 */
@Service
public class SysUserRoleService {
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    public List<SysUserRole> listByUserId(Integer userId) {
        return userRoleMapper.listByUserId(userId);
    }
}
