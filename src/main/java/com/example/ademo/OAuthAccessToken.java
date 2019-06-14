package com.example.ademo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

@Entity
@Table(name = "jdbc_oauth_access_token")
public class OAuthAccessToken {

    @Id
    private String tokenId;

    private String authenticationId, userName, clientId, refreshToken;

    private Byte[] token, authentication;

}
