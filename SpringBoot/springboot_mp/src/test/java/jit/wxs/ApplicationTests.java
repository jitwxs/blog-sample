package jit.wxs;

import jit.wxs.entity.User;
import jit.wxs.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
        List<User> users = userService.selectList(null);
        System.out.println(users);
    }

}
