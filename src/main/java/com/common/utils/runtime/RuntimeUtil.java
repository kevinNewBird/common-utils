package com.common.utils.runtime;


import java.io.*;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * description: org.vastdata
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/8/14
 * version: 1.0
 */
public class RuntimeUtil {

    public static final RuntimeResult exec(String[] commands) throws IOException {
        return exec(commands, null, null);
    }

    public static final RuntimeResult exec(String[] commands, String[] envp) throws IOException {
        return exec(commands, envp, null);
    }

    public static final RuntimeResult exec(String[] commands, File workdir) throws IOException {
        return exec(commands, null, workdir);
    }

    public static final RuntimeResult exec(String[] commands, String[] envp, File workdir) throws IOException {

        RuntimeResult result = new RuntimeResult();
        StringBuilder correctCache = new StringBuilder();
        StringBuilder errorCache = new StringBuilder();

        InputStream is = null;
        InputStream es = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(commands, envp, workdir);
            is = process.getInputStream();
            es = process.getErrorStream();
            // 必须要阻塞等待命令执行完成，否则可能会因为执行过快，导致流缓冲区无数据从而退出getMessageByStream
            int exitCode = process.waitFor();
//            new ExecTask(correctCache, is).run();
//            new ExecTask(errorCache, es).run();
            CountDownLatch latch = new CountDownLatch(2);
            Thread t1 = new Thread(new ExecTask(latch, correctCache, is));
            t1.setDaemon(true);
            t1.start();
            Thread t2 = new Thread(new ExecTask(latch, errorCache, es));
            t2.setDaemon(true);
            t2.start();

            latch.await();
            result.setExitCode(exitCode);
            result.setMessage(correctCache.append(errorCache).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(is)) {
                is.close();
            }
            if (Objects.nonNull(es)) {
                es.close();
            }
        }
        return result;
    }

    public static String getMessageByStream(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        int len = 0;
        byte[] cache = new byte[1024];
        StringBuilder ret = new StringBuilder();
        while (true) {
            while (is.available() > 0) {
                len = is.read(cache, 0, 1024);
                if (len <= 0) {
                    break;
                }
                ret.append(new String(cache, 0, len));
            }
            if (is.available() > 0) {
                len = is.read(cache, 0, 1024);
                ret.append(new String(cache, 0, len));
            } else {
                break;
            }
        }

        return ret.toString();
    }

    private static class ExecTask implements Runnable {

        private CountDownLatch latch;

        private InputStream is;

        private StringBuilder result;

        public ExecTask(CountDownLatch latch, StringBuilder result, InputStream is) {
            this.latch = latch;
            this.result = result;
            this.is = is;
        }

        @Override
        public void run() {
            try {
                result.append(getMessageByStream(is));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        RuntimeResult result = exec(new String[]{"cmd", "/C", "ping 127.0.0.1"});
        System.out.println(result);
    }
}
