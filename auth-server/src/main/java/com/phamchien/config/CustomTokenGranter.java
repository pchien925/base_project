package com.phamchien.config;

import com.phamchien.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CustomTokenGranter extends AbstractTokenGranter {

    private UserServiceImpl userService;
    private AuthenticationManager authenticationManager;

    protected CustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    public CustomTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType, UserServiceImpl userService) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        return super.getOAuth2Authentication(client, tokenRequest);
    }

    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        try {
            String grantType = tokenRequest.getGrantType();
            if (SecurityConstant.GRANT_TYPE_USER.equalsIgnoreCase(grantType)) {
                String phone = tokenRequest.getRequestParameters().get("phone");
                String password = tokenRequest.getRequestParameters().get("password");
                return userService.getAccessTokenForUser(client, tokenRequest, password, phone, this.getTokenServices());
            } else if (SecurityConstant.GRANT_TYPE_OTP.equalsIgnoreCase(grantType)) {
                String phone = tokenRequest.getRequestParameters().get("phone");
                String otp = tokenRequest.getRequestParameters().get("otp");
                return userService.getAccessTokenForOtp(client, tokenRequest, otp, phone, this.getTokenServices());
            }
            else{
                String username = tokenRequest.getRequestParameters().get("username");
                String password = tokenRequest.getRequestParameters().get("password");
                return userService.getAccessTokenForPasswordGrant(client, tokenRequest, username, password, this.getTokenServices());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new InvalidTokenException("account or tenant invalid");
        }
    }

}
