package com.example.mongodb.handler;

import com.example.mongodb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Component
@Aspect
@Slf4j
public class GlobalAspectHandler {
    @Around("within(com.example.mongodb.controller.*)")
    public Object logBefore(ProceedingJoinPoint point) throws Throwable {
        Object resultVal = point.proceed();

        long start = System.currentTimeMillis();
        long processTime = System.currentTimeMillis() - start;

        Object[] params = point.getArgs();

        String paramMessage = Arrays.stream(params).map(JsonUtils::toJson).collect(joining(", "));

        log.info("---------------------------------------------------------------------------------------------------------------------------");
        log.info("Processing Time({}) : {} ms", point.getSignature().toShortString(), processTime);
        log.info("Param : {}", paramMessage);
        log.info("Result : {}", JsonUtils.toJson(resultVal));
        log.info("---------------------------------------------------------------------------------------------------------------------------");

        return resultVal;
    }
}
