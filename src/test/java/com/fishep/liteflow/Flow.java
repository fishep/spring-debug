package com.fishep.liteflow;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @Author fly.fei
 * @Date 2024/8/16 16:55
 * @Desc
 **/
@Slf4j
public class Flow {

    public static void main(String[] args) {
        Path currentDirectoryPath = FileSystems.getDefault().getPath("");
        String currentDirectoryName = currentDirectoryPath.toAbsolutePath().toString();
        System.out.println("Current Directory = \"" + currentDirectoryName + "\"");

        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource("src/main/resources/config/flow.el.xml");
        FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);

        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
    }

}
