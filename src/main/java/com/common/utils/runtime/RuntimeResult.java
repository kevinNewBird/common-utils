package com.common.utils.runtime;

/**
 * description: org.vastdata.install.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/8/15
 * version: 1.0
 */
public class RuntimeResult {

    private int exitCode;

    private String message;

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RuntimeResult{" +
                "exitCode=" + exitCode +
                ", message='" + message + '\'' +
                '}';
    }
}
