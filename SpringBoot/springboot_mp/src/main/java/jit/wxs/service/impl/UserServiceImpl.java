package jit.wxs.service.impl;

import jit.wxs.entity.User;
import jit.wxs.mapper.UserMapper;
import jit.wxs.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
