package com.fishep.spring.debug.liteflow.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/8/16 16:21
 * @Desc
 **/
@Slf4j
@Component("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {

        log.info("process do your business");

    }

}
