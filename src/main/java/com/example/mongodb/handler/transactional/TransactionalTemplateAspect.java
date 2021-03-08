package com.example.mongodb.handler.transactional;

import com.example.mongodb.exception.ValidCustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;


/*
 * non-native transactions using Spring Data TransactionTemplate
 * */
@Component
@Aspect
@Slf4j
public class TransactionalTemplateAspect {
    private final MongoTransactionManager mongoTransactionManager;

    public TransactionalTemplateAspect(MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
        //We need to set SessionSynchronization to ALWAYS to use non-native Spring Data transactions.
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        this.mongoTransactionManager = mongoTransactionManager;
    }

    @Pointcut("@annotation(com.example.mongodb.handler.transactional.TemplateTransactional)")
    public void getTemplateTransactional() {
    }

    @Around("getTemplateTransactional()")
    public Object transactionalTemplate(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TemplateTransactional templateTransactional = AnnotationUtils.findAnnotation(method, TemplateTransactional.class);
        if (templateTransactional == null)
            return point.proceed();

        TransactionTemplate template = this.setTransactionalOption(templateTransactional);

        final Object[] result = {null};
        new TemporaryTransactionTemplateFactory(template).execute(() -> {
            try {
                result[0] = point.proceed();
            } catch (Throwable throwable) {
                throw new ValidCustomException(throwable.getMessage());
            }
        });

        return result[0];
    }

    // annotation name, value 동적으로 가져오는 방법을 찾아보자
    public TransactionTemplate setTransactionalOption(TemplateTransactional templateTransactional) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        if (templateTransactional.propagation().value() != Propagation.REQUIRED.value())
            transactionTemplate.setPropagationBehavior(templateTransactional.propagation().value());

        if (templateTransactional.isolation().value() != Isolation.DEFAULT.value())
            transactionTemplate.setIsolationLevel(templateTransactional.isolation().value());

        if (templateTransactional.readOnly())
            transactionTemplate.setReadOnly(true);

        if (templateTransactional.timeout() != TransactionDefinition.TIMEOUT_DEFAULT)
            transactionTemplate.setTimeout(templateTransactional.timeout());

        return transactionTemplate;
    }


}
