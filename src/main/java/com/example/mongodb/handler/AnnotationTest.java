package com.example.mongodb.handler;

import com.example.mongodb.handler.transactional.TemplateTransactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.lang.reflect.Method;

public class AnnotationTest {

    @TemplateTransactional(value = "transactional",
            isolation = Isolation.READ_UNCOMMITTED,
            propagation = Propagation.REQUIRED,
            timeout = 3,
            readOnly = true)
    public void testMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TemplateTransactional.class)) {
                System.out.println(method.getAnnotation(TemplateTransactional.class).value());
                System.out.println(method.getAnnotation(TemplateTransactional.class).isolation().value());
            }
        }
    }

    public static void main(String[] args) {
        new AnnotationTest().testMethod(AnnotationTest.class);
    }
}
