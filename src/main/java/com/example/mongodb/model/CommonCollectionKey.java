package com.example.mongodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;

public interface CommonCollectionKey<T> {

    @SuppressWarnings("unchecked")
    @JsonIgnore
    default T getCollectionKeyValue() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(CollectionKey.class)) { // @CollectionKey 있는지 체크
                field.setAccessible(true);
                try {
                    return (T) field.get(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    @JsonIgnore
    default String getCollectionKey() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(CollectionKey.class)) { // @CollectionKey 있는지 체크
                return field.getName();
            }
        }
        return "";
    }

    @JsonIgnore
    default void setValueByCollectionKey(T value) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(CollectionKey.class)) {
                field.setAccessible(true);
                try {
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @JsonIgnore
    default void setValueById(String value) {
        Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
