package com.fishep.spring.debug.liteflow.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/8/16 16:16
 * @Desc
 **/
@Slf4j
@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {

        log.info("process do your business");

    }

}
