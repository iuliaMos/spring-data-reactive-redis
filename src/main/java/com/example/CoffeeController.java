package com.example;

import com.example.entity.Coffee;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CoffeeController {

    private final ReactiveRedisOperations<String, Coffee> coffeOps;

    public CoffeeController(ReactiveRedisOperations<String, Coffee> coffeOps) {
        this.coffeOps = coffeOps;
    }

    @GetMapping("/coffee")
    public Flux<Coffee> all() {
        return coffeOps.keys("*")
                .flatMap(coffeOps.opsForValue()::get);
    }
}
