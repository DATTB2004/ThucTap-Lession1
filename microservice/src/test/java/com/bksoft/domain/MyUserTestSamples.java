package com.bksoft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MyUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MyUser getMyUserSample1() {
        return new MyUser().id(1L).idUser(1).userName("userName1").password("password1");
    }

    public static MyUser getMyUserSample2() {
        return new MyUser().id(2L).idUser(2).userName("userName2").password("password2");
    }

    public static MyUser getMyUserRandomSampleGenerator() {
        return new MyUser()
            .id(longCount.incrementAndGet())
            .idUser(intCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
