package org.selftest.interview.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AlgorithmApplication {

    public static void main(String[] args) {
//        BigDecimal a = new BigDecimal("5");
//        BigDecimal b = new BigDecimal("10");
//
//        BigDecimal[] c = a.divideAndRemainder(b);
//        System.out.println(c[0]+"===="+c[1]);

        System.out.println(f(6));

    }
    static Map<Integer,Integer> hasSolvedList = new HashMap<>();
    public static int f(int n){
        if(n ==1 ) return 1;
        if(n ==2) return 2;

        if(hasSolvedList.containsKey(n)){
            return hasSolvedList.get(n);
        }

        int ret = f(n-1)+f(n-2);
        hasSolvedList.put(n,ret);
        return ret;
    }

}

