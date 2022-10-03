package com.example.ssia.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .redirectUris("http://localhost:8080/home")

                .and()

                .withClient("client2")
                .secret("secret2")
                .authorities("authorization_code", "password", "refresh_token")
                .scopes("read")
                .redirectUris("http://localhost:8080/home");
    }
}
