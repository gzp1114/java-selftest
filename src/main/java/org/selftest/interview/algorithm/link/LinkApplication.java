package org.selftest.interview.algorithm.link;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkApplication {

    public static void main(String[] args) {
        DataChain dataChain = new DataChain(6);
//        printChain(reverse(dataChain.getHead()));
        printChain(reverseList(dataChain.getHead()));
    }

    public static void printChain(LinkNode head) {
        StringBuilder sb = new StringBuilder();
        LinkNode cur = head;
        sb.append(cur.getValue());
        while (null != cur.getNext()) {
            sb.append(" -> ");
            sb.append(cur.getNext().getValue());
            cur = cur.getNext();
        }
        System.out.println(sb.toString());
    }

    /**
     * 单链表反转(递归实现)
     * @param head
     * @return
     */
    public static LinkNode reverse(LinkNode head){
        if(head == null || head.next==null){
           return head;
        }
        LinkNode temp = head.next;
        LinkNode newHead = reverse(head.next);
        temp.next = head;
        head.next = null;
        return newHead;
    }

    /**
     * 单链表反转(遍历实现)
     * @param node
     * @return
     */
    public static LinkNode reverseList(LinkNode node){
        LinkNode pre = null;
        LinkNode next = null;
        while (node != null){
            next = node.next;
            node.next = pre;
            pre = node;
            node = next;
        }
        return pre;
    }

}

