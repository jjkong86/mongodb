package com.example.mongodb.utils;

import java.util.Date;

public class DateUtil {
    private DateUtil() {
    }
    private static final long ONE_MINUTE_TIME = 1000 * 60;
    private static final long ONE_HOUR_TIME = 1000 * 60 * 60;
    private static final long ONE_DAY_TIME = 1000 * 60 * 60 * 24;
    private static final long TTL_MAX_PERIOD = DateUtil.ONE_DAY_TIME * 90; //90일간 데이터 유지

    public static Date get90DayTime() {
        return new Date(System.currentTimeMillis() + TTL_MAX_PERIOD);
    }

    public static Date get1MinuteTime() {
        return new Date(System.currentTimeMillis() + ONE_MINUTE_TIME);
    }
}
