package com.common.statemachine.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMachine {

    // 用来记录要经过下一个状态需要的源状态与事件
    private final Map<String, String> translatedTransitionMap = new HashMap<>();

    // 用来记录状态变化的记录
    private final Set<String> translatedTransitions = new HashSet<>();

    private static final String DEFAULT_DELIMITER = "->";

    public StateMachine(Transition[] transitions) {
        for (Transition transition : transitions) {
            translatedTransitions.add(transition.getFrom() + DEFAULT_DELIMITER
                    + transition.getEvent() + DEFAULT_DELIMITER
                    + transition.getTo());

            if (translatedTransitionMap.containsKey(transition.getFrom() + DEFAULT_DELIMITER + transition.getEvent())) {
                throw new IllegalArgumentException("Duplicate transition found for "
                        + transition.getFrom() + DEFAULT_DELIMITER + transition.getEvent());
            }

            translatedTransitionMap.put(transition.getFrom() + DEFAULT_DELIMITER + transition.getEvent()
                    , transition.getTo());
        }
    }

    public String getNextState(String from, String event) {
        return translatedTransitionMap.get(from + DEFAULT_DELIMITER + event);
    }

    public boolean checkTransitions(String from, String event) {
        return translatedTransitions.contains(from + DEFAULT_DELIMITER + event);
    }
}
