package com.example.mongodb.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorFactory {
    private final ExecutorService executorService;
    private final List<Future<?>> list = new ArrayList<>();

    public ExecutorFactory(int threadCount) {
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void supportExecute(Callable<Void> callable) {
        this.list.add(this.executorService.submit(callable)); // 로직처리후 return 값을 담음
    }

    public void supportExecute(Runnable runnable) {
        this.list.add(this.executorService.submit(runnable));
    }

    public void executorWaitAndShutdown() {
        executorService.shutdown();
        try {
            // 처리중인 작업이 종료 되지 않으면 강제 종료 해줌
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        if (!CollectionUtils.isEmpty(list))
            list.forEach(ExecutorFactory::accept);
    }

    private static void accept(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
