package org.example.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BucketAsMapTest extends BaseTest {

    @Test
    void bucketAsMap() {
        Mono<Void> mapMono = this.client.getBuckets(StringCodec.INSTANCE)
                .get("user:1", "user:2").doOnNext(System.out::println).then();

        StepVerifier.create(mapMono).verifyComplete();
    }
}
