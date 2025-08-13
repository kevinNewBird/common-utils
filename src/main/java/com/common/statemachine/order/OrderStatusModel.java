package com.common.statemachine.order;

import com.common.statemachine.base.StateMachine;
import com.common.statemachine.base.Transition;

public class OrderStatusModel {

    private static final StateMachine ORDER_STATEMACHINE;

    static {

        ORDER_STATEMACHINE = new StateMachine(new Transition[]{
                new Transition(TradeStatusEnum.NEW.getStatus(), TradeEventEnum.CONFIRM.name(),TradeStatusEnum.WAIT_PAY.getStatus()),
                new Transition(TradeStatusEnum.NEW.getStatus(), TradeEventEnum.CANCEL.name(), TradeStatusEnum.CLOSED.getStatus()),
                new Transition(TradeStatusEnum.WAIT_PAY.getStatus(), TradeEventEnum.PAY_CANCEL.name(), TradeStatusEnum.PAY_SUCCESS.getStatus()),
                new Transition(TradeStatusEnum.WAIT_PAY.getStatus(), TradeEventEnum.PAY.name(),TradeStatusEnum.CANCELLED.getStatus()),
                new Transition(TradeStatusEnum.PAY_SUCCESS.getStatus(), TradeEventEnum.CONFIRM_SUCCESS.name(), TradeStatusEnum.COMPLETED.getStatus())
        });
    }

    public static String getTargetStatus(TradeStatusEnum currentStatus, TradeEventEnum tradeEvent) {
        return ORDER_STATEMACHINE.getNextState(currentStatus.getStatus(), tradeEvent.name());
    }
}
