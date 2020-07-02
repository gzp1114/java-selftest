package org.selftest.interview.design.service;

import org.selftest.interview.design.Observer.OrderEvent;
import org.selftest.interview.design.strategy.CalculateStrategy;
import org.selftest.interview.design.template.OrderTemplate;
import org.selftest.interview.design.template.TicketOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guozhipeng
 * @date 2020/7/2 14:23
 */
@Service
public class OrderService {

    @Autowired
    ApplicationContext applicationContext;

    private Map<String,CalculateStrategy> map = new HashMap<>();

    /**
     *  策略模式
     * @param list
     */
    public OrderService(List<CalculateStrategy> list) {
        for (CalculateStrategy calculateStrategy : list){
            map.put(calculateStrategy.type(),calculateStrategy);
        }
    }

    /**
     * 事件通知，使用观察者模式
     */
    public void createOrder(String type,double price){
        System.out.println("创建订单开始");

        //订单创建，使用模板模式
        OrderTemplate orderTemplate = new TicketOrder();
        orderTemplate.createOrder(map.get(type),price);

        //事件通知，使用观察者模式
        applicationContext.publishEvent(new OrderEvent("订单被创建了"));

        System.out.println("创建订单结束");

    }

}
