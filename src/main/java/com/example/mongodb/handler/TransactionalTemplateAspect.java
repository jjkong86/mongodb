package com.example.mongodb.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class TransactionalTemplateAspect {
    @Around("@annotation(com.example.mongodb.utils.TemplateTransactional)")
    public Object transactionalTemplate(ProceedingJoinPoint point) throws Throwable {
        return point.proceed();
    }
}
