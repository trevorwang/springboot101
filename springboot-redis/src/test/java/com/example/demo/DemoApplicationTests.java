package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

@SpringBootTest
class DemoApplicationTests {


    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisDataService redisHash;

    @Test
    void contextLoads() {
    }


    @Test
    public void testRedisWrite() {
        User user = new User();
        user.setUid("111");
        user.setName("111");
        user.setToken("xxxxxxx");
        redisHash.saveUser(user);

        Object o = redisTemplate.opsForHash().get("user:profile:user", "111");

        User u = (User) o;
        Assert.isTrue(u != null && "111".equals(u.getUid()), "有问题");
    }


    @Test
    public void testSetValue() {
        User user = new User();
        user.setUid("111");
        user.setName("111");
        user.setToken("xxxxxxx");
        redisHash.saveUser(user);
        User user2 = redisHash.getUser(user.getUid());
        assert user.equals(user2);
    }

}
