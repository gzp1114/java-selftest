package org.selftest.interview.design.strategy;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:34
 */
@Service
public class NormalCalculateStrategy implements CalculateStrategy {
    @Override
    public String type() {
        return "normal";
    }

    @Override
    public double calculatePrice(double price) {
        return price;
    }
}
