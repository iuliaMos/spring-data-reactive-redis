package com.example;

import com.example.entity.Coffee;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class CoffeeLoader {

    private ReactiveRedisOperations<String, Coffee> coffeeOps;
    private ReactiveRedisConnectionFactory factory;

    public CoffeeLoader(ReactiveRedisOperations<String, Coffee> coffeeOps, ReactiveRedisConnectionFactory factory) {
        this.coffeeOps = coffeeOps;
        this.factory = factory;
    }

    @PostConstruct
    public void postConstruct() {
        factory.getReactiveConnection().serverCommands().flushAll()
                .thenMany(Flux.just("Jet Black Redis", "Darth Redis", "Black Alert Redis")
                    .map(name -> new Coffee(UUID.randomUUID().toString(), name))
                    .flatMap(coffee -> coffeeOps.opsForValue().set(coffee.getId(), coffee)))
                .thenMany(coffeeOps.keys("*")
                    .flatMap(coffeeOps.opsForValue()::get))
                .subscribe(System.out::println);


    }
}
