package com.phamchien.config;

import com.phamchien.exception.oauth.CustomOauthException;
import com.phamchien.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${qrauth.auth.signing.key}")
    private String signingKey;

    @Value("${jwt.rsa.private-key}")
    private Resource privateKeyResource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    ObjectMapper objectMapper;

    private KeyPair keyPair;

    @PostConstruct
    public void init() throws Exception {
        byte[] privateKeyBytes = privateKeyResource.getInputStream().readAllBytes();
        String privateKeyPEM = new String(privateKeyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                rsaPrivateKey.getModulus(),
                rsaPrivateKey.getPublicExponent()
        );
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        this.keyPair = new KeyPair(publicKey, privateKey);
    }


    @Bean
    public TokenStore tokenStore() {
        JdbcTokenStore j = new JdbcTokenStore(jdbcTemplate.getDataSource());
        j.setAuthenticationKeyGenerator(new CustomAuthenticationKeyGenerator());
        return j;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(new CustomTokenConverter());
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(jdbcTemplate.getDataSource());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(jdbcTemplate,objectMapper), accessTokenConverter()));
        endpoints
                .pathMapping("/oauth/authorize", "/api/authorize")
                .pathMapping("/oauth/token", "/api/token")
                .authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain)
                .tokenGranter(tokenGranter(endpoints))
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore())
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService)
                .exceptionTranslator((exception -> {
                            if (exception instanceof OAuth2Exception) {
                                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
                                return ResponseEntity
                                        .status(oAuth2Exception.getHttpErrorCode())
                                        .body(new CustomOauthException(oAuth2Exception.getMessage()));
                            } else {
                                throw exception;
                            }
                        })
                );
    }

    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<TokenGranter>(Arrays.asList(endpoints.getTokenGranter()));
        granters.add(new CustomTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), "multi-tenant", userService));
        granters.add(new CustomTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), SecurityConstant.GRANT_TYPE_USER, userService));
        granters.add(new CustomTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), SecurityConstant.GRANT_TYPE_OTP, userService));
        return new CompositeTokenGranter(granters);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
        oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
        oauthServer.checkTokenAccess("permitAll()");
    }

}