package com.example.mongodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
public abstract class CommonModel<T> implements CommonCollectionKey<T> {
    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public long createTime;

    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public long updateTime;
}
