package com.common.factory.behavior;

import com.common.factory.behavior.enums.PStatus;
import com.common.factory.behavior.ifs.IBehavior;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: com.common.factory.behavior
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class BehaviorTree {

    private final IBehavior root;

    private final Map<String,Object> params; // 参数存储（包含输入参数、流程控制标识等）

    private final Map<String,List<Object>> inputs; // 前端交互参数


    private final Map<String,Object> effects; // 结果获取

    private final Map<String, List<String>> callbacks; // 回滚命令存储

    public BehaviorTree(IBehavior root) {
        this.root = root;
        this.params = new HashMap<>();
        this.effects = new HashMap<>();
        this.callbacks = new HashMap<>();
        this.inputs = new HashMap<>();
    }

    public void tick() {
        root.tick(this);
    }


}
