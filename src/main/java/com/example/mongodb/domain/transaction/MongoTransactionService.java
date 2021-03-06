package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.UserTransactionService;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserResponse;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.handler.transactional.TransactionTemplateFactoryBean;
import com.example.mongodb.utils.TemplateTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
@Slf4j
public class MongoTransactionService {
    private final TransactionTemplateFactoryBean transactionTemplateFactoryBean;
    private final UserService userService;
    private final UserTransactionService userTransactionService;

    public MongoTransactionService(TransactionTemplateFactoryBean transactionTemplateFactoryBean, UserService userService, UserTransactionService userTransactionService) {
        this.transactionTemplateFactoryBean = transactionTemplateFactoryBean;
        this.userService = userService;
        this.userTransactionService = userTransactionService;
    }

    public UserResponse saveUserWithTransaction(User user) {
        transactionTemplateFactoryBean.execute(() -> {
            userTransactionService.dataSaveTemplate(user);
            userTransactionService.saveUserLogWithTtlIndex(user);
        });

        return UserResponse.builder().user(user).build();
    }

    public UserResponse rollBackTest(User user, boolean isRollBack) {
        try {
            transactionTemplateFactoryBean.execute(() -> {
                userTransactionService.dataSaveTemplate(user);
                userTransactionService.saveUserLogWithTtlIndex(user);
                if (isRollBack)
                    throw new ValidCustomException(500, "data rollback");
            });

        } catch (RuntimeException e) {
            if (!isRollBack) // 의도적인 오류가 아님
                throw new ValidCustomException(e.getMessage());
        }

        // rollback 성공이면 exception 발생함
        userTransactionService.findUserByUserId(user.getUserId());

        return UserResponse.builder().user(user).build();
    }


    @TemplateTransactional(propagation = Propagation.REQUIRED, timeout = 3)
    public UserResponse annotationRollBackTest(User user, boolean isRollBack) {
        userTransactionService.dataSaveTemplate(user);
        userTransactionService.saveUserLogWithTtlIndex(user);
        if (isRollBack)
            throw new ValidCustomException(500, "data rollback");

        // rollback 성공이면 exception 발생함
        userTransactionService.findUserByUserId(user.getUserId());

        return UserResponse.builder().user(user).build();
    }

    @TemplateTransactional(propagation = Propagation.REQUIRED, timeout = 3)
    public UserResponse rollBackWithRequiresNew(User user, boolean isRollBack) {
        try {
            transactionTemplateFactoryBean.execute(() -> {
                userTransactionService.dataSaveTemplate(user);
                userTransactionService.saveUserLogWithTtlIndex(user);
                userTransactionService.requiresNewTemplate(user);
                if (isRollBack)
                    throw new ValidCustomException(500, "data rollback");
            });

        } catch (RuntimeException e) {
            if (!isRollBack) // 의도적인 오류가 아님
                throw new ValidCustomException(e.getMessage());
        }

        // rollback 성공이면 exception 발생함
        User findUser = userService.findUserByUserId(user.getUserId());
        if (findUser != null)
            log.info(findUser.toString());

        return UserResponse.builder().user(user).build();
    }
}
