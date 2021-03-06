package cn.watsontech.webhelper.common.security.authentication;

import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.security.TokenService;
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
