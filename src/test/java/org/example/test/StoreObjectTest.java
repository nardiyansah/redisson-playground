package org.example.test;

import org.example.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StoreObjectTest extends BaseTest {

    @Test
    void storeObject() {
        RBucketReactive<Student> bucket = this.client.getBucket("student:1",
                new TypedJsonJacksonCodec(Student.class));
        Student student = new Student("Ardi", 24, "Lampung");

        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
