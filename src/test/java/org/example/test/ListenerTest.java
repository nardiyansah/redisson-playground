package org.example.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class ListenerTest extends BaseTest {

    @Test
    void listenEvent() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);

        Mono<Void> set = bucket.set("sam", 5, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        Mono<Void> event = bucket.addListener((ExpiredObjectListener) name -> System.out.println("Expired key : " + name)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteEvent() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);

        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        Mono<Void> event = bucket.addListener((DeletedObjectListener) name -> System.out.println("Deleted key : " + name)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
