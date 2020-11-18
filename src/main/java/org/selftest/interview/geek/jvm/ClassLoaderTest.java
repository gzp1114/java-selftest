package org.selftest.interview.geek.jvm;

import java.lang.reflect.Method;

/**
 * @author guozhipeng
 * @date 2020/11/17 11:24
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException {
        SelfClassLoader loader = new SelfClassLoader();
        Class<?> aClass = loader.findClass("Hello");
        try {
            Object obj = aClass.newInstance();
            Method method = aClass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
