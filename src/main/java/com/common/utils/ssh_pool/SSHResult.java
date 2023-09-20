package com.common.utils.ssh_pool;

/**
 * description: com.common.utils.ssh_pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/9/20
 * version: 1.0
 */
public class SSHResult {

    private int statusCode;

    private String message;

    private SSHResult() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class SSHResultHolder {
        private final SSHResult instance = new SSHResult();

        public SSHResultHolder code(int code) {
            instance.setStatusCode(code);
            return this;
        }

        public SSHResultHolder message(String message) {
            instance.setMessage(message);
            return this;
        }

        public SSHResult build() {
            return instance;
        }

    }

}
