package com.own.onlinemall.auth.controller;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class SocialUser {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String scope;
    private long created_at;
}
