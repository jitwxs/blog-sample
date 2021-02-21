package com.github.jitwxs.sample.mp3;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jitwxs.sample.mp3.enity.User;
import com.github.jitwxs.sample.mp3.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Mp3ApplicationTest {
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