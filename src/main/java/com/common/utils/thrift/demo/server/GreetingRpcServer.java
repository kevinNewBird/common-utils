package com.common.utils.thrift.demo.server;

import com.common.utils.thrift.demo.service.GreetingService;
import com.common.utils.thrift.demo.service.impl.GreetingServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * description: com.common.utils.thrift.demo.server
 * company: 北京海量数据有限公司
 * create by: zhaosong 2025/1/13
 * version: 1.0
 */
public class GreetingRpcServer {

    /**
     * description: rpc服务端
     * <tip/>
     * 博客：https://my.oschina.net/wangmengjun/blog/910817
     * create by: zhaosong 2025/1/13 15:36
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            TServerSocket serverTransport = new TServerSocket(9090);
            TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();

            // 关联处理器于GreetingService服务实现
            TProcessor processor = new GreetingService.Processor<>(new GreetingServiceImpl());
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
            serverArgs.processor(processor);
            serverArgs.protocolFactory(factory);

            TServer server = new TThreadPoolServer(serverArgs);
            System.out.printf("Start server on port 9090....%n");

            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
