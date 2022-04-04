package org.example.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ListTest extends BaseTest {

    @Test
    void addList() {
        RListReactive<Long> list = this.client.getList("list-number", LongCodec.INSTANCE);

        Mono<Void> listAdd = Flux.range(1, 10)
                .map(Long::valueOf)
                .flatMap(aLong -> list.add(aLong))
                .then();

        StepVerifier.create(listAdd).verifyComplete();
    }

    @Test
    void queueTest() {
        RQueueReactive<Long> queue = this.client.getQueue("list-number", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll().repeat(3).doOnNext(System.out::println).then();

        StepVerifier.create(queuePoll).verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(2)
                .verifyComplete();
    }
}
