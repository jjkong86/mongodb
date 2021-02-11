package com.example.mongodb.domain.user.model;

import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.utils.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@Document(collection = "UserLog")
public class UserLog extends User {
    public Date logCreateTime; // ttl index

    public UserLog(User user) {
        super(user);
        this.logCreateTime = DateUtil.get90DayTime();
    }

    public UserLog(User user, Date minuteTime) {
        super(user);
        this.logCreateTime = minuteTime;
    }
}
