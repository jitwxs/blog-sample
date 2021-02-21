package com.github.jitwxs.sample.jwt.ch02.service;

import com.github.jitwxs.sample.jwt.ch02.entity.SysUser;
import com.github.jitwxs.sample.jwt.ch02.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jitwxs
 * @date 2018/3/30 1:26
 */
@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    public SysUser selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public SysUser selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
