package org.selftest.interview.algorithm.link;

/**
 * @author guozhipeng
 * @date 2020/7/30 17:37
 */
public class DataChain {

    private  LinkNode head;

    public DataChain(int size) {
        LinkNode head = new LinkNode(0);
        LinkNode cur = head;
        for (int i = 1; i < size; i++) {
            LinkNode tmp = new LinkNode(i);
            cur.setNext(tmp);
            cur = tmp;
        }
        this.head = head;
    }

    public LinkNode getHead() {
        return head;
    }

    public void setHead(LinkNode head) {
        this.head = head;
    }



    public static void main(String... strings) {
        DataChain chain = new DataChain(10);
//        printChain(chain.getHead());
    }

}
