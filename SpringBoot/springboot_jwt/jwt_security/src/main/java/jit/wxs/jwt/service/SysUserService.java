package jit.wxs.jwt.service;

import jit.wxs.jwt.entity.SysUser;
import jit.wxs.jwt.mapper.SysUserMapper;
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
