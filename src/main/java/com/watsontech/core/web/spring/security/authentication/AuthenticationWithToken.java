/*
 * Copyright (c) 2020. 串普网络科技有限公司版权所有.
 */

package com.watsontech.core.web.spring.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {
    public AuthenticationWithToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
        setAuthenticated(aPrincipal!=null);
    }

    public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    @Autowired
    public void setToken(String token) {
        setDetails(token);
    }

    @Autowired
    public String getToken() {
        return (String)getDetails();
    }
}
