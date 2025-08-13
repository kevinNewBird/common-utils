package com.common.statemachine;

import com.common.statemachine.order.TradeCreateRequest;
import com.common.statemachine.order.TradeOrder;

public class TestMain {

    public static void main(String[] args) {
        TradeOrder order = new TradeOrder();
        order.confirm(new TradeCreateRequest());
        System.out.println(order.getStatus());
    }
}
