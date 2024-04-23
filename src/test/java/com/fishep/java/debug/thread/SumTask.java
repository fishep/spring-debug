package com.fishep.java.debug.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * @Author fly.fei
 * @Date 2024/4/16 15:49
 * @Desc
 **/
@Slf4j
public class SumTask extends RecursiveTask<Long> {

    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // 如果任务足够小,直接计算:
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += this.array[i];
            }

            // 故意放慢计算速度:
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }

            log.trace("start: {}, end: {}, sum: {}", start, end, sum);

            return sum;
        }

//        ForkJoinTask<Long> fork = fork();

        return forkJoinTask();
    }

    protected Long forkJoinTask() {
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
        log.trace(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subtask2);
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        Long result = subresult1 + subresult2;
        log.trace("result = " + subresult1 + " + " + subresult2 + " ==> " + result);
        return result;
    }

}
