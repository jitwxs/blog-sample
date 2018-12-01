package jit.wxs.demo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jit.wxs.demo.entity.User;
import jit.wxs.demo.mapper.UserMapper;
import jit.wxs.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
