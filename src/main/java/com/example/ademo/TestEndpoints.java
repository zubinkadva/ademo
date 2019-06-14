package com.example.ademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class TestEndpoints {

    @Autowired
    private DefaultTokenServices tokenServices;

    @GetMapping(path = "/secure")
    public String secure() {
        return "This is a secure path";
    }

    @DeleteMapping(path = "/oauth/revoke")
    public Map<String, String> revoke(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        tokenServices.revokeToken(userToken);
        return Collections.singletonMap("message", "Successfully logged out");
    }
}
