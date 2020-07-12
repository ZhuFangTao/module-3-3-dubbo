package com.lagou.dubbo.invoke;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import service.TestTpService;

import java.util.concurrent.*;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午5:29
 * \
 */

@Component
public class InvokeComponent implements Runnable {

    @Reference(filter = {"-filter,monitorFilter"})
    private TestTpService testTpService;

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    public InvokeComponent() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                testTpService.methodA();
                testTpService.methodB();
                testTpService.methodC();
            });
        }
    }
}