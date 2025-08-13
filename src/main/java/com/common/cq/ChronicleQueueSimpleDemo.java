package com.common.cq;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.UnrecoverableTimeoutException;

public class ChronicleQueueSimpleDemo {
    public static final String TEMP_CQ_DIR = "cq_temp/";

    /**
     * desc： 高速缓存队列ChronicleQueue的简单使用
     * @param args
     */
    public static void main(String[] args) {
        // 1.创建队列实例
        try (ChronicleQueue queue = SingleChronicleQueueBuilder.single(TEMP_CQ_DIR).build();) {
            // 2.写入队列数据
            ExcerptAppender appender = queue.createAppender();
            appender.writeText("Hello world");

            // 3.读取队列数据
            ExcerptTailer tailer = queue.createTailer();
            String content = tailer.readText();
            System.out.println("read content: " + content);
        } catch (UnrecoverableTimeoutException e) {
            e.printStackTrace();
        }

    }

}
