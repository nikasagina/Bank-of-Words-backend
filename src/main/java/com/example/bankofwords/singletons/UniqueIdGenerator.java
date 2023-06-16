package com.example.bankofwords.singletons;


import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class UniqueIdGenerator {
    private final AtomicLong counter;

    private static UniqueIdGenerator instance;

    private UniqueIdGenerator() {
        counter = new AtomicLong();
    }

    public static UniqueIdGenerator getInstance() {
        if (instance == null) {
            instance = new UniqueIdGenerator();
        }

        return instance;
    }

    public long generateUniqueId() {
        return counter.incrementAndGet();
    }
}
