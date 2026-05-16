package com.codit.talktalkcoach.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenExpiry;   // ms 단위 (예: 1800000 = 30분)
    private long refreshTokenExpiry;  // ms 단위 (예: 1209600000 = 14일)

    public void setSecret(String secret) { this.secret = secret; }
    public void setAccessTokenExpiry(long v) { this.accessTokenExpiry = v; }
    public void setRefreshTokenExpiry(long v) { this.refreshTokenExpiry = v; }
}
