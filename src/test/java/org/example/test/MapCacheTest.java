package org.example.test;

import org.example.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class MapCacheTest extends BaseTest {

    @Test
    void testExpireMap() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = this.client.getMapCache("user:cache", codec);

        Mono<Student> s1 = mapCache.put(1, new Student("andi", 24, "lumajang"), 5, TimeUnit.SECONDS);
        Mono<Student> s2 = mapCache.put(2, new Student("budi", 23, "lamongan"), 10, TimeUnit.SECONDS);

        StepVerifier.create(s1.then(s2).then()).verifyComplete();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }
}
