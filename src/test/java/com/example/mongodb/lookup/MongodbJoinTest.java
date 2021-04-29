package com.example.mongodb.lookup;

import com.example.mongodb.ServiceTest;
import com.example.mongodb.domain.user.model.User;
import com.example.mongodb.domain.user.model.UserDesc;
import com.example.mongodb.utils.ExecutorFactory;
import com.mongodb.BasicDBObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MongodbJoinTest extends ServiceTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    private final String userIdFieldName = new User().getCollectionKey();
    private final String userDescParentId = "parentId";
    private final String lookUpFieldName = "userDesc_docs";

    //    @Before
    public void setUp() {
        List<User> userList = new ArrayList<>();
        List<UserDesc> userDescList = new ArrayList<>();

        for (int i = 300; i < 100000; i++) {
//            userList.add(new User(i, "name" + i));
            userDescList.add(new UserDesc(i - 300, i, "desc" + i));
        }
//        mongoTemplate.insertAll(userList);
        mongoTemplate.insertAll(userDescList);
    }

    @Test
    public void lookUp_성능_테스트() {
        long time = System.currentTimeMillis();
        for (int i = 300; i < 400; i++) {
            mongoTemplate.findOne(new Query().addCriteria(Criteria.where(userIdFieldName).is(i)), User.class);
            mongoTemplate.findOne(new Query().addCriteria(Criteria.where(userDescParentId).is(i)), UserDesc.class);
        }

        System.out.println("단건 조회 시간 : " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        this.lookup();
        System.out.println("lookUp 조회 시간 : " + (System.currentTimeMillis() - time));
    }

    @Test
    public void lookup() {
        LookupOperation lookupOperation = LookupOperation.newLookup().
                from(UserDesc.class.getSimpleName()).
                localField(userIdFieldName).
                foreignField(userDescParentId).
                as(lookUpFieldName);

        for (int i = 300; i < 400; i++) {
            AggregationOperation match = Aggregation.match(Criteria.where(userIdFieldName).is(i));
            Aggregation aggregation = Aggregation.newAggregation(lookupOperation, match);
            List<BasicDBObject> results = mongoTemplate.aggregate(aggregation, User.class.getSimpleName(), BasicDBObject.class).getMappedResults();
            System.out.println(results);
        }
    }

    @Test
    public void lookupWithConcurrent() {
        int threadCount = 50;
        ExecutorFactory executorWrapper = new ExecutorFactory(threadCount);
        BlockingQueue<Long> supplyQueue = new LinkedBlockingDeque<>();
        for (int i = 300; i < 1000; i++) {
            supplyQueue.offer((long) i);
        }

        LookupOperation lookupOperation = LookupOperation.newLookup().
                from(UserDesc.class.getSimpleName()).
                localField(userIdFieldName).
                foreignField(userDescParentId).
                as(lookUpFieldName);

        for (int i = 0; i < threadCount; i++) {
            while (!supplyQueue.isEmpty()) {
                long userId = supplyQueue.poll();
                executorWrapper.supportExecute(() -> {
                    AggregationOperation match = Aggregation.match(Criteria.where(userIdFieldName).is(userId));
                    Aggregation aggregation = Aggregation.newAggregation(lookupOperation, match);
                    List<BasicDBObject> results = mongoTemplate.aggregate(aggregation, User.class.getSimpleName(), BasicDBObject.class).getMappedResults();

                });
            }
        }

        executorWrapper.executorWaitAndShutdown();
    }
}
