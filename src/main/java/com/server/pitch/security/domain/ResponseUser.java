package com.server.pitch.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    private String user_id;
    private String user_email;
    private String provider;
    private String user_pw;
    private String user_nm;
    private String role;
}
