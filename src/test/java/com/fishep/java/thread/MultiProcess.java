package com.fishep.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author fly.fei
 * @Date 2024/5/25 10:40
 * @Desc
 **/
@Slf4j
public class MultiProcess {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 创建 ProcessBuilder 对象，指定命令和参数
        ProcessBuilder pb = new ProcessBuilder("ls");

        // 设置工作目录
//        pb.directory(new File("/tmp"));

        // 启动进程
        Process process = pb.start();

        int waitFor = process.waitFor();
        log.info("waitFor: " + waitFor);

        int exitValue = process.exitValue();
        log.info("exitValue: " + exitValue);
    }

}
