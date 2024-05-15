package com.fishep.testfixture.thread;

import java.util.concurrent.RecursiveTask;

/**
 * @Author fly.fei
 * @Date 2024/4/16 15:49
 * @Desc
 **/
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

//            try {
//                TimeUnit.SECONDS.sleep(1L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            return sum;
        }

//        ForkJoinTask<Long> fork = fork();

        return forkJoinTask();
    }

    protected Long forkJoinTask() {
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subtask2);
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        Long result = subresult1 + subresult2;
        return result;
    }

}
