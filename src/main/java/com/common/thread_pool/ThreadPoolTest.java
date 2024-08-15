package com.common.thread_pool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * description: com.common.thread_pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/5
 * version: 1.0
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor(new CommonThreadFactory("CommonPool", false));
        try {
            executor.execute(()->{
                ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(1
                        , new CommonThreadFactory("SchedulePool", true));
                try {

                    schedulePool.scheduleWithFixedDelay(() -> {
                        System.out.println("date: " + System.currentTimeMillis());
                    }, 0, 1, TimeUnit.SECONDS);
                    // 守护线程，确保当前线程的执行不要立刻结束，从而导致定时任务线程的回收
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    schedulePool.shutdown();
                }

            });

        } finally {
            executor.shutdown();
        }

        System.in.read();
    }


}

