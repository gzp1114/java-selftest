package org.selftest.interview.jvm;

public class ReferenceCountGc {

    public Object instance = null;

    public static void main(String[] args) {
        ReferenceCountGc objA = new ReferenceCountGc();
        ReferenceCountGc objB = new ReferenceCountGc();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        //打印gc VM命令：-verbose:gc -XX:+PrintGCDetails

        System.gc();

    }


}
