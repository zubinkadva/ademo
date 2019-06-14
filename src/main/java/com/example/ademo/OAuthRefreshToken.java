package com.example.ademo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jdbc_oauth_refresh_token")
public class OAuthRefreshToken {

    @Id
    private String tokenId;

    private Byte[] token, authentication;
}
