package org.selftest.interview.design.strategy;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:32
 */
public interface CalculateStrategy {

    public String type();

    public double calculatePrice(double price);

}
