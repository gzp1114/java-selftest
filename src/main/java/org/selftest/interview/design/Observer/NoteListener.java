package org.selftest.interview.design.Observer;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author guozhipeng
 * @date 2020/7/2 14:31
 */
@Component
public class NoteListener implements ApplicationListener<OrderEvent> {
    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(orderEvent.getSource()+"====推送通知");
    }
}
