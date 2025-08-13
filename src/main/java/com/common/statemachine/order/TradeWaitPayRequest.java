package com.common.statemachine.order;

public class TradeWaitPayRequest extends BaseRequest{

    @Override
    TradeStatusEnum getTradeStatus() {
        return TradeStatusEnum.WAIT_PAY;
    }
}
