package com.server.pitch.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "token", timeToLive = 7 * 24 * 60 * 60)
public class RefreshToken implements Serializable {


    @Id
    private String refreshToken;

    @Indexed
    private String user_id;

    @Indexed
    private String accessToken;

}
