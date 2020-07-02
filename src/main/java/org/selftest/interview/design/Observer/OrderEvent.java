package org.selftest.interview.design.Observer;

import org.springframework.context.ApplicationEvent;

/**
 * @author guozhipeng
 * @date 2020/7/2 14:24
 */
public class OrderEvent extends ApplicationEvent {

    public OrderEvent(Object source) {
        super(source);
    }
}
