package com.common.exceptions;

/**
 * description: com.common.exceptions
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/9/20
 * version: 1.0
 */
public class SSHException extends RuntimeException {

    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }
}
