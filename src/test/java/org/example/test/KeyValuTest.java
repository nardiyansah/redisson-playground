package org.example.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class KeyValuTest extends BaseTest {

    @Test
    void TestKeyValue() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> sam = bucket.set("sam");

        StepVerifier.create(sam.concatWith(bucket.get().doOnNext(System.out::println).then()))
                .verifyComplete();
    }

    @Test
    void TestKeyValueExpiry() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> sam = bucket.set("sam", 5, TimeUnit.SECONDS);

        StepVerifier.create(sam.concatWith(bucket.get().doOnNext(System.out::println).then()))
                .verifyComplete();
    }

    @Test
    void TestKeyValueExpiryExtend() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> sam = bucket.set("sam", 20, TimeUnit.SECONDS);

        StepVerifier.create(sam.concatWith(bucket.get().doOnNext(System.out::println).then()))
                .verifyComplete();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Mono<Boolean> booleanMono = bucket.expire(60, TimeUnit.SECONDS);

        StepVerifier.create(booleanMono)
                .expectNext(true)
                .verifyComplete();

        // access expire time
        Mono<Long> ttl = bucket.remainTimeToLive();

        StepVerifier.create(ttl)
                .consumeNextWith(System.out::println)
                .verifyComplete();
    }
}
