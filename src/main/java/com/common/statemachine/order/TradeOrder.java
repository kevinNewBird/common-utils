package com.common.statemachine.order;

import lombok.Data;

@Data
public class TradeOrder {

    private String orderId;
    private String status;
    private double price;

    public TradeOrder confirm(TradeCreateRequest request){
        this.setStatus(OrderStatusModel.getTargetStatus(request.getTradeStatus(), TradeEventEnum.CONFIRM));
        return this;
    }

    public TradeOrder pay(TradeWaitPayRequest request){
        this.setStatus(OrderStatusModel.getTargetStatus(request.getTradeStatus(), TradeEventEnum.CANCEL));
        return this;
    }

}
