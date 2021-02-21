package com.github.jitwxs.sample.jwt.ch02.service;

import com.github.jitwxs.sample.jwt.ch02.entity.SysRole;
import com.github.jitwxs.sample.jwt.ch02.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jitwxs
 * @date 2018/3/30 1:27
 */
@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    public SysRole selectById(Integer id){
        return roleMapper.selectById(id);
    }
}
