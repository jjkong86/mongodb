package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserResponse;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.utils.TemplateTransactional;
import com.example.mongodb.utils.TransactionTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

@Service
@Slf4j
public class MongoTransactionService {
    private final TransactionTemplateFactory transactionTemplateFactory;

    private final UserService userService;

    public MongoTransactionService(TransactionTemplateFactory transactionTemplateFactory, UserService userService) {
        this.transactionTemplateFactory = transactionTemplateFactory;
        this.userService = userService;
    }

    public UserResponse saveUserWithTransaction(User user) {
        transactionTemplateFactory.execute(() -> {
            userService.saveUserHandling(user);
            userService.saveUserLogWithTtlIndex(user);
        });

        return UserResponse.builder().user(user).build();
    }

    public UserResponse rollBackTest(User user, boolean isRollBack) {
        try {
            transactionTemplateFactory.execute(() -> {
                userService.saveUserHandling(user);
                userService.saveUserLogWithTtlIndex(user);
                if (isRollBack)
                    throw new ValidCustomException(500, "data rollback");
            });

        } catch (RuntimeException e) {
            if (!isRollBack) // 의도적인 오류가 아님
                throw new ValidCustomException(e.getMessage());
        }

        // rollback 성공이면 exception 발생함
        userService.findUserByUserId(user.getUserId());

        return UserResponse.builder().user(user).build();
    }


    @TemplateTransactional(
            isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED, timeout = 3, readOnly = true)
    public UserResponse annotationRollBackTest(User user) {
        userService.saveUserHandling(user);
        userService.saveUserLogWithTtlIndex(user);

        // rollback 성공이면 exception 발생함
        userService.findUserByUserId(user.getUserId());

        return UserResponse.builder().user(user).build();
    }
}
