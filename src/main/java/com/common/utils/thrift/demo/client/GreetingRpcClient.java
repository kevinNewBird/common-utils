package com.common.utils.thrift.demo.client;

import com.common.utils.thrift.demo.service.GreetingService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

/**
 * description: com.common.utils.thrift.demo.service
 * company: 北京海量数据有限公司
 * create by: zhaosong 2025/1/13
 * version: 1.0
 */
public class GreetingRpcClient {

    /**
     * description: rpc客户端
     * <tip/>
     * 博客：https://my.oschina.net/wangmengjun/blog/910817
     * create by: zhaosong 2025/1/13 15:36
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            TSocket transport = new TSocket("127.0.0.1", 9090);
            transport.open();
            TBinaryProtocol protocol = new TBinaryProtocol(transport);

            GreetingService.Client client = new GreetingService.Client(protocol);

            String name = "Eric";
            System.out.printf("请求参数===> name为 %s%n", name);
            String result = client.sayHello("Eric");
            System.out.printf("返回结果===> 为 %s%n", result);
            transport.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
