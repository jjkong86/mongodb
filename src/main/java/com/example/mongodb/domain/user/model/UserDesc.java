package com.example.mongodb.domain.user.model;

import com.example.mongodb.model.CollectionKey;
import com.example.mongodb.model.CommonModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = "UserDesc")
public class UserDesc extends CommonModel<Long> {
    @CollectionKey
    @Indexed(unique = true)
    private long seq;
    private long parentId;
    private String desc;

    public UserDesc(long seq, long parentId, String desc) {
        this.seq = seq;
        this.parentId = parentId;
        this.desc = desc;
    }
}
