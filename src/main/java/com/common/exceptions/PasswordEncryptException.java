package com.common.exceptions;

/**
 * description: com.common.exceptions
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/9/20
 * version: 1.0
 */
public class PasswordEncryptException extends RuntimeException{
    public PasswordEncryptException(String message) {
        super(message);
    }

    public PasswordEncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
