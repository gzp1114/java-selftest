package org.selftest.interview.design.template;

/**
 * @author guozhipeng
 * @date 2020/7/2 15:18
 */
public class TicketOrder extends OrderTemplate {

    @Override
    public void saveOrder() {
        System.out.println("保存影票订单信息");
    }
}
