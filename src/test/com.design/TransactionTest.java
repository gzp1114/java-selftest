package com.design;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selftest.interview.transaction.Service.UserService;
import org.selftest.interview.transaction.TransactionApplication;
import org.selftest.interview.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

/**
 * @author guozhipeng
 * @date 2020/7/2 14:37
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
public class TransactionTest {

    @Autowired
    UserService userService;

    @Test
    public void insertUser() throws SQLException {
        User user = new User();
        user.setId(1006L);
        user.setUserName("peng6");
        userService.insertUser(user);
    }

    @Test
    public void deleteUser() throws SQLException {
        userService.deleteUser(1001L);
    }

    @Test
    public void deleteUser2() throws SQLException {
        userService.deleteUser2(1004L);
    }

}
