package org.selftest.interview.algorithm.link;

/**
 * @author guozhipeng
 * @date 2020/7/30 17:30
 */
public class LinkNode {
    public int value;
    public LinkNode next;

    public LinkNode(int data) {
        this.value = data;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LinkNode getNext() {
        return next;
    }

    public void setNext(LinkNode next) {
        this.next = next;
    }
}
