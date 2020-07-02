package org.selftest.interview.design.template;

import org.selftest.interview.design.strategy.CalculateStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:13
 */
public abstract class OrderTemplate {

    public void createOrder(CalculateStrategy calculateStrategy, double price){
        //计算金额，策略模式
        double discountPrice = calculateStrategy.calculatePrice(price);
        System.out.println("订单计算金额"+discountPrice);
        //保存订单信息
        this.saveOrder();
    }

    /**
     * 保存订单
     */
    public abstract void saveOrder();

}
