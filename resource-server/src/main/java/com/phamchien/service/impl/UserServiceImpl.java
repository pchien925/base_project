package com.phamchien.service.impl;

import com.phamchien.jwt.UserBaseJwt;
import com.phamchien.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
@Slf4j
public class UserServiceImpl {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;


    public UserBaseJwt getAddInfoFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2AuthenticationDetails oauthDetails =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            if (oauthDetails != null) {
                Map<String, Object> map = (Map<String, Object>) oauthDetails.getDecodedDetails();
                String encodedData = (String) map.get("additional_info");
                //idStr -> json
                if (encodedData != null && !encodedData.isEmpty()) {
                    return UserBaseJwt.decode(encodedData);
                }
                return null;
            }
        }
        return null;
    }

}
