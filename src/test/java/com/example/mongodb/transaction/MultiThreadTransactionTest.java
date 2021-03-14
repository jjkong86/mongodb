package com.example.mongodb.transaction;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.handler.transactional.TemporaryTransactionTemplateFactory;
import com.example.mongodb.handler.transactional.TransactionTemplateFactory;
import com.example.mongodb.handler.transactional.TransactionTemplateFactoryBean;
import com.example.mongodb.utils.ExecutorFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;


public class MultiThreadTransactionTest extends TransactionalTemplateService {

    @Autowired
    private TransactionTemplateFactoryBean transactionTemplateFactoryBean;

    @Autowired
    private MongoTransactionManager mongoTransactionManager;


    @Test
    public void saveUserMultiThreadTransactionTest() {
        ExecutorFactory executorFactory = new ExecutorFactory(2);

        executorFactory.supportExecute(() -> this.addUsers(false, transactionTemplateFactoryBean));
        executorFactory.supportExecute(() -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
            transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
            this.addUsers(true, new TemporaryTransactionTemplateFactory(transactionTemplate));
        });

        executorFactory.executorWaitAndShutdown();
    }

/*
* 테스트 결과 :
* Propagation REQUIRED, REQUIRES_NEW option과 관계 없이
* 특정 다큐먼트에 트랜잭션을 먼저 선점 하더라도 변경이 발생하면 update는 발생하지 않는다.
* */
    @Test
    public void updateUserMultiThreadTransactionTest() {
        ExecutorFactory executorFactory = new ExecutorFactory(2);

        executorFactory.supportExecute(() -> {
            long time = System.currentTimeMillis();

            this.updateUsers(false, transactionTemplateFactoryBean, "bean");
            this.timeSleep(5);

            System.out.println("first time : " + (System.currentTimeMillis() - time));
        });

        executorFactory.supportExecute(() -> {
            long time = System.currentTimeMillis();
            this.timeSleep(2);

            TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
            transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
            this.updateUsers(false, new TemporaryTransactionTemplateFactory(transactionTemplate), "new");

            System.out.println("second time : " + (System.currentTimeMillis() - time));
        });

        executorFactory.executorWaitAndShutdown();
    }

    public void addUsers(boolean rollback, TransactionTemplateFactory transactionTemplateFactory) {
//        for (int i = 0; i < 100; i++) {
//            // given
//            User user = new User((long) i, i + "test", "010-" + i);
//
//            // when
//            try {
//                transactionTemplateFactory.execute(() -> {
//                    userService.saveUserHandling(user);
//                    userService.saveUserLogWithTtlIndex(user);
//
//                    //rollback
//                    if (rollback)
//                        throw new ValidCustomException(500, "data rollback");
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void updateUsers(boolean rollback, TransactionTemplateFactory transactionTemplateFactory, String name) {
        for (int i = 0; i < 1; i++) {
            // given
            User user = new User((long) 1, i + "test" + name, "010-" + i);

            // when
            try {
                transactionTemplateFactory.execute(() -> {
                    userService.updateUserByCollectionKey(user);

                    //rollback
                    if (rollback)
                        throw new ValidCustomException(500, "data rollback");
                });

            } catch (Exception ignored) {
            }
        }
    }

    public void timeSleep(long timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
