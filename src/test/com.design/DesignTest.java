package com.design;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selftest.interview.design.DesignApplication;
import org.selftest.interview.design.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author guozhipeng
 * @date 2020/7/2 14:37
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DesignApplication.class)
public class DesignTest {

    @Autowired
    OrderService orderService;

    @Test
    public void observerTest(){
        orderService.createOrder("vip",10);
    }

}
