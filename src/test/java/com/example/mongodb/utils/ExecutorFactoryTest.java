package com.example.mongodb.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ExecutorFactoryTest {

    @Test
    public void executorFactoryTest() {
        //given
        ExecutorFactory executorFactory = new ExecutorFactory(20);

        //when
        for (int i = 0; i < 20; i++) {
            final int index = i;
            executorFactory.supportExecute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " start : " + index);
                    TimeUnit.SECONDS.sleep(index);
                    System.out.println(Thread.currentThread().getName() + " end : " + index);
                } catch (InterruptedException ignore) {
                }
            });
        }

        try {
            executorFactory.executorWaitAndShutdown();
        } catch (Exception e) {
            // then
            Assert.assertThrows(RuntimeException.class, () -> System.out.println("sleep interrupted."));
        }
    }
}
