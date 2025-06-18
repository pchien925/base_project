package com.phamchien.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Value("${jwt.rsa.public-key}")
    private Resource publicKeyResource;

    @Autowired
    private JsonToUrlEncodedAuthenticationFilter jsonFilter;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        try {
            String publicKey = StreamUtils.copyToString(publicKeyResource.getInputStream(), StandardCharsets.UTF_8);
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setAccessTokenConverter(new CustomTokenConverter());
            converter.setVerifierKey(publicKey);
            return converter;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key for JWT verification", e);
        }
    }

    @Bean
    public TokenStore tokenStore()  {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(jsonFilter, BasicAuthenticationFilter.class)
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/index", "/pub/**", "/api/token","/api/auth/pwd/verify-token",
                        "/api/auth/activate/resend", "/api/auth/pwd", "/api/auth/logout", "/actuator/**").permitAll()
                .antMatchers( "/v1/customer/register").permitAll()
                .antMatchers("/v1/service/detail/**").permitAll()
                .antMatchers("/v1/user/login","/v1/user/signup").permitAll()
                .antMatchers("/v1/account/request_forget_password", "/v1/account/forget_password").permitAll()
                .antMatchers("/**").authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("phamchien");
        resources.tokenStore(tokenStore());
    }
}