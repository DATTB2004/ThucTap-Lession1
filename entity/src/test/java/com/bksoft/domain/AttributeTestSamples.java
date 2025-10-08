package com.bksoft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AttributeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Attribute getAttributeSample1() {
        return new Attribute().id(1L).name("name1");
    }

    public static Attribute getAttributeSample2() {
        return new Attribute().id(2L).name("name2");
    }

    public static Attribute getAttributeRandomSampleGenerator() {
        return new Attribute().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
