package org.selftest.interview.test;

import com.alibaba.fastjson.JSON;
import org.apache.tools.ant.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestClass {

    public static void main(String[] args) throws ParseException {
        List<A> list = new ArrayList<>();
        A a1 = new A();
        a1.setEndTime(new Date());
        list.add(a1);
        A a2 = new A();
        a2.setEndTime(DateUtil.parseTime("2020-08-01 00:00:00"));
        list.add(a2);
        A a3 = new A();
        a3.setEndTime(DateUtil.parseTime("2020-08-30 00:00:00"));
        list.add(a3);

        list.sort((a,b) -> a.getEndTime().after(b.getEndTime())?1:0);

        System.out.println(JSON.toJSONString(list));

//        Long num = 300000000000000072L;
//        Long addNum = num;
//        Long nextNum = num+1;
//        for (int i = 0; i < 20; i++) {
//            addNum = num+9587432932040000L;
//            System.out.println(num+"=========="+addNum);
//        }

//        System.out.println(stringToInt("-1999999999"));

//        System.out.println(Integer.parseInt(""));

    }

    public static int stringToInt (String str){
        char[] num = str.toCharArray();//得到各个字符的char
        int result = 0;
        boolean negative = false;
        if(num[0] == 43){ //+
            negative = false;
        }else if(num[0] == 45){ //-
            negative = true;
        }

        for(int i = 1; i < num.length; i++){
            if(num[i]>58||num[i]<48){//0~9对应的Ascall码
                System.out.println("数据格式错误，转换失败！！！");
                throw new NumberFormatException();
            }else{
                result+=(num[i]-48)*(Math.pow(10, num.length-i-1));
            }
        }
        return negative?-result:result;
    }


}
