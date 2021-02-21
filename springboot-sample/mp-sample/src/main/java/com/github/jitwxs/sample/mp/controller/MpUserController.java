package com.github.jitwxs.sample.mp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.jitwxs.sample.mp.enity.Msg;
import com.github.jitwxs.sample.mp.enity.User;
import com.github.jitwxs.sample.mp.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-19
 */
@RestController
public class MpUserController {

    @Autowired
    private UserService userService;

    private <T> Msg<T> prepareMsg(Integer code, Boolean status, String info, T data){
        Msg<T> msg = new Msg<>();
        msg.setCode(code);
        msg.setStatus(status);
        msg.setInfo(info);
        msg.setData(data);
        return msg;
    }

    @GetMapping("/users")
    public Msg<Page<User>> listUser(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {

        Page<User> userPage = userService.selectPage(new Page<>(page, size), new EntityWrapper<>());

        return prepareMsg(Msg.OK, true,null,userPage);
    }

    @GetMapping("/users/{id}")
    public Msg<User> getUserById(@PathVariable("id") String id) {
        User user = userService.selectById(id);

        if(user == null) {
            return prepareMsg(Msg.OK, false, "为查找到指定用户", null);
        } else {
            return prepareMsg(Msg.OK, true, null, user);
        }
    }

    @GetMapping("/users/name/{name}")
    public Msg<List<User>> findUserByName(@PathVariable("name") String name) {
        List<User> list = userService.selectList(new EntityWrapper<User>()
                .like("name", name));

        if(list.size() == 0) {
            return prepareMsg(Msg.OK, false,"未搜索到结果",null);
        } else {
            return prepareMsg(Msg.OK, true,"搜索到" + list.size() + "条结果",list);
        }
    }

    @DeleteMapping("/users/{id}")
    public Msg<User> deleteUserById(@PathVariable("id") String id) {
        User user = userService.selectById(id);

        if(user == null) {
            return prepareMsg(Msg.OK, false,"用户不存在",null);
        } else {
            Boolean b = userService.deleteById(id);
            if(!b) {
                return prepareMsg(Msg.OK, false,"删除失败",null);
            } else {
                return prepareMsg(Msg.OK, true,"删除成功",user);
            }
        }
    }

    @PostMapping("/users")
    public Msg<User> insertUser(User user) {
        // 用户名不能为空
        if(StringUtils.isBlank(user.getName())) {
            return prepareMsg(Msg.OK, false,"昵称为空",null);
        } else {
            user.setCreateDate(new Date());
            boolean b = userService.insert(user);
            if(!b) {
                return prepareMsg(Msg.OK, false,"添加失败",null);
            } else {
                return prepareMsg(Msg.OK, true,"添加成功",user);
            }
        }
    }

    @PutMapping("/users")
    public Msg<User> updateUser(User user) {
        String id = user.getId();
        if(StringUtils.isBlank(id) || userService.selectById(id) == null) {
            return prepareMsg(Msg.OK, false,"用户不存在",null);
        } else {
            if(StringUtils.isBlank(user.getName())) {
                return prepareMsg(Msg.OK, false,"昵称为空",null);
            } else {
                boolean b = userService.updateById(user);
                if(!b) {
                    return prepareMsg(Msg.OK, false,"更新失败",null);
                } else {
                    return prepareMsg(Msg.OK, true,"更新成功",userService.selectById(id));
                }
            }
        }
    }

}

