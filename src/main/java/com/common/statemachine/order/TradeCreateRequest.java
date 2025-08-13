package com.common.statemachine.order;

public class TradeCreateRequest extends BaseRequest{

    @Override
    TradeStatusEnum getTradeStatus() {
        return TradeStatusEnum.NEW;
    }
}
