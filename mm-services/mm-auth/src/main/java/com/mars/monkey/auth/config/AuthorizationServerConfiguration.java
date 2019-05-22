package com.mars.monkey.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/*

/oauth/authorize
/oauth/token
/oauth/check_token
/oauth/confirm_access
/oauth/error
/oauth/token_key

*/

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //        password 方案一：明文存储，用于测试，不能用于生产
        String finalSecret = "123456";
        //        password 方案二：用 BCrypt 对密码编码
        //        String finalSecret = new BCryptPasswordEncoder().encode("123456");
        // password 方案三：支持多种编码，通过密码的前缀区分编码方式
        //        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
        //配置两个客户端,一个用于password认证一个用于client认证
        // @formatter:off
        clients.inMemory().withClient("client_1")
               .authorizedGrantTypes("client_credentials", "refresh_token")
               .scopes("select")
               .authorities("oauth2")
               .secret(finalSecret)
               .and().withClient("client_2")
               .authorizedGrantTypes("password", "refresh_token")
               .scopes("select")
               .authorities("oauth2")
               .accessTokenValiditySeconds(60*60)
               .secret(finalSecret);
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // @formatter:off
        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        // @formatter:off
        oauthServer
                .tokenKeyAccess("permitAll()")         //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
                .allowFormAuthenticationForClients();
        // @formatter:on
    }

}


