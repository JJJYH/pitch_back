package com.server.pitch.sort.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailConfig {
    @Value("${email.user}")
    private String userEmail;

    @Value("${email.password}")
    private String userPassword;

    @Value("${email.smtp_host}")
    private String smtpHost;

    @Value("${email.smtp_port}")
    private int smtpPort;
}
