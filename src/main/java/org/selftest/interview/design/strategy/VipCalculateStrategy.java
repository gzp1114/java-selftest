package org.selftest.interview.design.strategy;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:34
 */
@Service
public class VipCalculateStrategy implements CalculateStrategy {
    @Override
    public String type() {
        return "vip";
    }

    @Override
    public double calculatePrice(double price) {
        return price * 0.8;
    }

}
