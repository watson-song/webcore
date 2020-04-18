/*
 * Copyright (c) 2020. 串普网络科技有限公司版权所有.
 */
package com.watsontech.core.web.spring.security.authentication;

import com.watsontech.core.web.spring.security.LoginUser;
import com.watsontech.core.web.spring.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.Token;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Optional;

public class TokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> tokenOptional = (Optional) authentication.getPrincipal();
        if (!tokenOptional.isPresent() || tokenOptional.get().isEmpty()) {
            throw new BadCredentialsException("无效Token");
        }

        String key = tokenOptional.get();
        Token token = tokenService.verifyToken(key);

        LoginUser loginUser = userDetailsService.loginByUserId(token.getExtendedInformation());
        return new AuthenticationWithToken(loginUser, token, loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

}
