package com.server.pitch.security.repository;

import com.server.pitch.security.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByAccessToken(String accessToken);

    RefreshToken findByUser_id(String user_id);
}
