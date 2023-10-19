package com.server.pitch.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SecurityRepository {
    private final RedisTemplate<String, String> template;

    public void save(String accessKey, String RefreshKey){
        template.opsForValue().set(accessKey,RefreshKey);
    }
    public String findByKey(String key){
        return template.opsForValue().get(key);
    }
}
