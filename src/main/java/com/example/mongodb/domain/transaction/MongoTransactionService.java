package com.example.mongodb.domain.transaction;

import com.example.mongodb.domain.user.UserService;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.response.UserResponse;
import com.example.mongodb.exception.ValidCustomException;
import com.example.mongodb.utils.TransactionTemplateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MongoTransactionService {
    private final TransactionTemplateWrapper transactionTemplateWrapper;

    private final UserService userService;

    public MongoTransactionService(TransactionTemplateWrapper transactionTemplateWrapper, UserService userService) {
        this.transactionTemplateWrapper = transactionTemplateWrapper;
        this.userService = userService;
    }

    public UserResponse saveUserWithTransaction(User user) {
        transactionTemplateWrapper.execute(() -> {
            userService.saveUserHandling(user);
            userService.saveUserLogWithTtlIndex(user);
        });

        return UserResponse.builder().user(user).build();
    }

    public UserResponse rollBackTest(User user, boolean isRollBack) {
        try {
            transactionTemplateWrapper.execute(() -> {
                userService.saveUserHandling(user);
                userService.saveUserLogWithTtlIndex(user);
                if (isRollBack)
                    throw new ValidCustomException(500, "data rollback");
            });

        } catch (RuntimeException e) {
            e.printStackTrace();
            if (!isRollBack) // 의도적인 오류가 아님
                throw new ValidCustomException(e.getMessage());
        }

        // rollback 성공이면 exception 발생함
        userService.findUserByUserId(user.getUserId());

        return UserResponse.builder().user(user).build();
    }

}
