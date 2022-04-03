package org.example.test;

import org.example.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.util.Map;

public class MapTest extends BaseTest {

    @Test
    void mapTest1() {
        RMapReactive<String, String> map = this.client.getMap("user:1", StringCodec.INSTANCE);
        Map<String, String> user = Map.of(
                "name", "sam",
                "age", "2",
                "address", "atlanta");

        Mono<Void> profile = map.putAll(user).then();

        StepVerifier.create(profile).verifyComplete();
    }

    @Test
    void mapTest2() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Object, Object> map = this.client.getMap("users", codec);

        Mono<Void> users = map.putAll(Map.of(
                new Student("andi", 3, "bandung"),
                new Student("budi", 4, "jakarta")
        ));

        StepVerifier.create(users).verifyComplete();
    }
}
