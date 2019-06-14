package com.example.ademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${client-id}")
    private String clientId;

    @Value("${client-secret}")
    private String clientSecret;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomService customService;

    /*@Autowired
    TokenStore tokenStore;*/

    @Autowired
    DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients(); // so that client id and secret is passed in body of form
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .jdbc(dataSource)
                .withClient(clientId)
                .secret(encoder.encode(clientSecret))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
        .userDetailsService(customService);
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    TokenStore tokenStore() {
        String insertAccessTokenSql = "insert into jdbc_oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)";
        String selectAccessTokenSql = "select token_id, token from jdbc_oauth_access_token where token_id = ?";
        String selectAccessTokenAuthenticationSql = "select token_id, authentication from jdbc_oauth_access_token where token_id = ?";
        String selectAccessTokenFromAuthenticationSql = "select token_id, token from jdbc_oauth_access_token where authentication_id = ?";
        String selectAccessTokensFromUserNameAndClientIdSql = "select token_id, token from jdbc_oauth_access_token where user_name = ? and client_id = ?";
        String selectAccessTokensFromUserNameSql = "select token_id, token from jdbc_oauth_access_token where user_name = ?";
        String selectAccessTokensFromClientIdSql = "select token_id, token from jdbc_oauth_access_token where client_id = ?";
        String deleteAccessTokenSql = "delete from jdbc_oauth_access_token where token_id = ?";

        String insertRefreshTokenSql = "insert into jdbc_oauth_refresh_token (token_id, token, authentication) values (?, ?, ?)";
        String selectRefreshTokenSql = "select token_id, token from jdbc_oauth_refresh_token where token_id = ?";
        String selectRefreshTokenAuthenticationSql = "select token_id, authentication from jdbc_oauth_refresh_token where token_id = ?";
        String deleteRefreshTokenSql = "delete from jdbc_oauth_refresh_token where token_id = ?";

        String deleteAccessTokenFromRefreshTokenSql = "delete from jdbc_oauth_access_token where refresh_token = ?";

        JdbcTokenStore jdbcTokenStore  = new JdbcTokenStore(dataSource);
        jdbcTokenStore.setInsertAccessTokenSql(insertAccessTokenSql);
        jdbcTokenStore.setSelectAccessTokenSql(selectAccessTokenSql);
        jdbcTokenStore.setSelectAccessTokenAuthenticationSql(selectAccessTokenAuthenticationSql);
        jdbcTokenStore.setSelectAccessTokenFromAuthenticationSql(selectAccessTokenFromAuthenticationSql);
        jdbcTokenStore.setSelectAccessTokensFromUserNameAndClientIdSql(selectAccessTokensFromUserNameAndClientIdSql);
        jdbcTokenStore.setSelectAccessTokensFromUserNameSql(selectAccessTokensFromUserNameSql);
        jdbcTokenStore.setSelectAccessTokensFromClientIdSql(selectAccessTokensFromClientIdSql);
        jdbcTokenStore.setDeleteAccessTokenSql(deleteAccessTokenSql);

        jdbcTokenStore.setInsertRefreshTokenSql(insertRefreshTokenSql);
        jdbcTokenStore.setSelectRefreshTokenSql(selectRefreshTokenSql);
        jdbcTokenStore.setSelectRefreshTokenAuthenticationSql(selectRefreshTokenAuthenticationSql);
        jdbcTokenStore.setDeleteRefreshTokenSql(deleteRefreshTokenSql);
        jdbcTokenStore.setDeleteAccessTokenFromRefreshTokenSql(deleteAccessTokenFromRefreshTokenSql);

        return jdbcTokenStore;
    }

}

