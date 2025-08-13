package com.common.statemachine.order;

public enum TradeEventEnum {

    /**
     * 订单确认
     */
    CONFIRM,

    /**
     * 取消订单
     */
    CANCEL,

    /**
     * 支付
     */
    PAY,

    /**
     * 支付取消
     */
    PAY_CANCEL,

    /**
     * 订单确认成功
     */
    CONFIRM_SUCCESS,

    /**
     * 订单完成
     * `
     */
    FINISH;
}
