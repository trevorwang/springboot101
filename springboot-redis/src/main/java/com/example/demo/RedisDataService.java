package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisDataService {
    final RedisTemplate<String, Object> template2;
    final ObjectMapper mapper;

    final static String USER_PROFILE_KEY = "user:profile:user";

    public void saveUser(User user) {
        saveData(USER_PROFILE_KEY, user.getUid(), user);
        template2.expire(USER_PROFILE_KEY, 300, TimeUnit.SECONDS);
    }

    public User getUser(String key) {
        return getData(USER_PROFILE_KEY, key, User.class);
    }


    public <T> void saveData(String key, String field, T value) {
        template2.opsForHash().put(key, field, value);
    }

    public <T> T getData(String key, String field, Class<T> toValueType) {
        Object o = template2.opsForHash().get(key, field);
        return mapper.convertValue(o, toValueType);
    }

}

