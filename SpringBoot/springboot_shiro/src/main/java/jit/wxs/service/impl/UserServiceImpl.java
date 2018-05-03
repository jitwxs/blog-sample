package jit.wxs.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.entity.User;
import jit.wxs.mapper.UserMapper;
import jit.wxs.service.IUserService;
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

