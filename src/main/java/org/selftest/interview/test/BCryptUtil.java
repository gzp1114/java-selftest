package org.selftest.interview.test;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
    /**
     * 用户输入密码
     * @param pwd
     * @return
     */
    public static String encode(String pwd){
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        return hashed;
    }

    /**
     *
     * @param pwd 用户输入密码
     * @param hashed 数据库密码
     * @return true表示密码正确，false表示错误
     */
    public static boolean check(String pwd,String hashed){
        boolean flag=false;
        try{
            flag=BCrypt.checkpw(pwd, hashed);
        }catch (Exception e){
            flag=false;
        }
        return flag;
    }
    public static void main(String[] args) {

        boolean f = BCryptUtil.check(MD5Util.encrypt(MD5Util.encrypt("000000")), "$2a$10$2RK1LJab0Rz3OZI8wrL6hODbD0IU4BE8IhB5gKW9/dBz9uNNRAEcS");
        System.out.println("========="+f);

//        long startTime = System.currentTimeMillis();
//            String origin = "14e1b600b1fd579f47433b88e8d85291";
//            BCryptUtil.encode("14e1b600b1fd579f47433b88e8d85291");
//        long endTime0 = System.currentTimeMillis();
//        System.out.println(endTime0-startTime);
        }
    }
