package com.example.mongodb.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
// 클래스의 field에 @CollectionKey 선언 후 리플렉션을 활용하여 id값 제어

@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CollectionKey {

}
