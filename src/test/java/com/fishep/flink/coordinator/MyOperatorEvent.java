package com.fishep.flink.coordinator;

import cn.hutool.core.util.IdUtil;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;

/**
 * @Author fly.fei
 * @Date 2024/8/6 18:25
 * @Desc
 **/
public class MyOperatorEvent implements OperatorEvent {

    protected Long id;

    public MyOperatorEvent() {
        id = IdUtil.getSnowflakeNextId();
    }

    @Override
    public String toString() {
        return "MyOperatorEvent{" +
            "id=" + id +
            '}';
    }

}
