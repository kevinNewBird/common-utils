package com.common.jni;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * description: com.common.jni
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/6/18
 * version: 1.0
 */
public class Hello {

    static {
        // 正常c语言生成动态库是要加-c，但是java的jni一定不要使用，否则会报错：only ET_DYN and ET_EXEC can be loaded
        // 注意：生成动态库时，一定不要使用 -c
        //gcc -fPIC -shared -I E:\Java\jdk-11.0.9\include -I E:\Java\jdk-11.0.9\include\win32 -o hello.dll Hello.c
        try {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    URL url = ClassLoader.getSystemResource("hello.dll");
                    System.load(url.getPath());
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public native void sayHello();
}
