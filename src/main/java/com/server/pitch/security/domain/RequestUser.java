package com.server.pitch.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    //private String user_id;
    private String user_email;
    private String user_pw;
}
