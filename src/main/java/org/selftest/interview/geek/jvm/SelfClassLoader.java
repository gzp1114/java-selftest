package org.selftest.interview.geek.jvm;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author guozhipeng
 * @date 2020/11/17 11:10
 */
public class SelfClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            Resource resource = new ClassPathResource("geek/Hello.xlass");
            if(!resource.exists()) {
                System.out.println("文件不存在");
            }
            File sourceFile =resource.getFile();
            FileInputStream is = new FileInputStream(sourceFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] cLassBytes =   bos.toByteArray();
            is.close();
            bos.close();

            for (int i = 0; i < cLassBytes.length; ++i) {
                cLassBytes[i] = (byte) (255 - cLassBytes[i]);
            }

            Class clazz = defineClass(name, cLassBytes, 0, cLassBytes.length);
            return clazz;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }

    public byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }

}
