package org.selftest.interview.design.template;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:18
 */
public class GoodsOrder extends OrderTemplate {

    @Override
    public void saveOrder() {
        System.out.println("保存卖品订单信息");
    }
}
