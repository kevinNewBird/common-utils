package com.common.utils.thrift.demo.service.impl;

import com.common.utils.thrift.demo.service.GreetingService;
import org.apache.thrift.TException;

/**
 * description: 编写服务接口实现类
 * company: 北京海量数据有限公司
 * create by: zhaosong 2025/1/13
 * version: 1.0
 */
public class GreetingServiceImpl implements GreetingService.Iface {

    @Override
    public String sayHello(String name) throws TException {
        System.out.printf("调用sayHello方法的参数 name = {%s}%n", name);
        return "Hello, " + name;
    }
}
