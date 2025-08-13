package com.common.statemachine.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transition {

    // 起始状态
    private String from;

    // 动作/事件
    private String event;

    // 目标状态
    private String to;
}
