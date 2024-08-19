package com.fishep.spring.debug.liteflow;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/8/16 16:27
 * @Desc
 **/
@Component
public class YourClass {

    @Resource
    private FlowExecutor flowExecutor;

    public void testConfig(){
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
    }

}
