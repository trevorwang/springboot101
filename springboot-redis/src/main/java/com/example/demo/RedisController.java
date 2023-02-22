package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
public class RedisController {

    final
    StringRedisTemplate template;

    final
    RedisConnection redisConnection;

    public RedisController(StringRedisTemplate template, RedisConnection redisConnection) {
        this.template = template;
        this.redisConnection = redisConnection;
    }

    @GetMapping("/save/{what}")
    public String saveString(@PathVariable String what) {
        template.opsForList().leftPush("aaaa", what);
        return template.opsForList().rightPop("aaaa");
    }


    @GetMapping("/hash/set")
    public String hashSet() {
        redisConnection.hashCommands().hSet("hash1".getBytes(), "field1".getBytes(), "value".getBytes());
        return new String(redisConnection.hashCommands().hGet("hash1".getBytes(), "field1".getBytes()));
    }


}
