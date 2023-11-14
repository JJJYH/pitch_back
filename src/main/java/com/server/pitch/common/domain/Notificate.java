package com.server.pitch.common.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "notificate", timeToLive = 7 * 24 * 60 * 60)
public class Notificate implements Serializable {
    @Id
    String id;
    @Indexed
    @JsonProperty("user_id")
    String userId;
    String text;
    String url;
    @Indexed
    Date date;
}
