package jit.wxs.demo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jit.wxs.demo.entity.User;
import jit.wxs.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
        Page<User> page = new Page<>(1, 10);
        page.setAsc("name");
        IPage<User> selectPage = userMapper.selectPage(page, null);
        System.out.println(selectPage.getRecords());
    }
}