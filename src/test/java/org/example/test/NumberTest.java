package org.example.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class NumberTest extends BaseTest {

    @Test
    void number() {
        RAtomicLongReactive atomicLong = this.client.getAtomicLong("user:1:age");

        Mono<Void> incAndGet = atomicLong.incrementAndGet().doOnNext(System.out::println).then();

        StepVerifier.create(incAndGet).verifyComplete();
    }
}
