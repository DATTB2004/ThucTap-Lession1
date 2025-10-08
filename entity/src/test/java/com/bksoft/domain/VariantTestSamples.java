package com.bksoft.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Variant getVariantSample1() {
        return new Variant().id(1L).stock(1);
    }

    public static Variant getVariantSample2() {
        return new Variant().id(2L).stock(2);
    }

    public static Variant getVariantRandomSampleGenerator() {
        return new Variant().id(longCount.incrementAndGet()).stock(intCount.incrementAndGet());
    }
}
