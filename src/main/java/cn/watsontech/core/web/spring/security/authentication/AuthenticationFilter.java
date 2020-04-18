package cn.watsontech.core.web.spring.security.authentication;

import cn.watsontech.core.web.result.Result;
import cn.watsontech.core.web.spring.security.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.watsontech.core.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationFilter extends GenericFilterBean {
    static final String TOKEN_SESSION_KEY = "token";
    static final String LOGIN_USERID_KEY = "loginUserId";
    static final String REQUEST_URI_KEY = "req.requestURI";
    static final String REQUEST_QUERYSTRING_KEY = "req.queryString";
    static final String REQUEST_FULL_KEY = "req.requestURIWithQueryString";
    static final String REQUEST_SEQUENCEID_KEY = "req.id";
    static final String REQUEST_REMOTEADDR_KEY = "req.remoteAddr";

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> token = Optional.ofNullable(httpRequest.getHeader("X-Auth-Token"));
        if (!token.isPresent()) {
            token = Optional.ofNullable(httpRequest.getParameter("token"));
        }

        try {
            if (token.isPresent()&& StringUtils.isNotEmpty(token.get())) {
                MDC.put(TOKEN_SESSION_KEY, token.get());
                logger.debug("Trying to authenticate user by X-Auth-Token method. Token: {}", token);
                processTokenAuthentication(token);
            }

            logger.debug("AuthenticationFilter is passing request down the filter chain");
            addSessionContextToLogging(request);
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();

            AuthenticationFilter.handleAuthenticationServiceException(httpRequest, httpResponse, internalAuthenticationServiceException);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();

            AuthenticationFilter.handleUnAuthentication(httpRequest, httpResponse, authenticationException);
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(LOGIN_USERID_KEY);
            MDC.remove(REQUEST_URI_KEY);
            MDC.remove(REQUEST_QUERYSTRING_KEY);
            MDC.remove(REQUEST_FULL_KEY);
            MDC.remove(REQUEST_SEQUENCEID_KEY);
            MDC.remove(REQUEST_REMOTEADDR_KEY);
        }
    }

    private void addSessionContextToLogging(ServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userValue = "EMPTY";
        if (authentication != null && !org.springframework.util.StringUtils.isEmpty(authentication.getPrincipal().toString())) {
            try {
                LoginUser user = ((LoginUser)(authentication.getPrincipal()));
                userValue = user.getId()+"@"+user.getUserType();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        MDC.put(LOGIN_USERID_KEY, userValue);

        if (request instanceof HttpServletRequest) {
            MDC.put(REQUEST_URI_KEY, StringUtils.defaultString(((HttpServletRequest) request).getRequestURI()));
            MDC.put(REQUEST_QUERYSTRING_KEY, StringUtils.defaultString(((HttpServletRequest) request).getQueryString()));
            MDC.put(REQUEST_FULL_KEY, "["+((HttpServletRequest) request).getMethod()+"("+((HttpServletRequest) request).getRequestedSessionId()+")]"+((HttpServletRequest) request).getRequestURI() + (((HttpServletRequest) request).getQueryString() == null ? "" : "?"+((HttpServletRequest) request).getQueryString()));
        }

        //为每一个请求创建一个ID，方便查找日志时可以根据ID查找出一个http请求所有相关日志
        MDC.put(REQUEST_SEQUENCEID_KEY, StringUtils.remove(UUID.randomUUID().toString(),"-"));
        MDC.put(REQUEST_REMOTEADDR_KEY, StringUtils.defaultString(String.valueOf(request.getAttribute("X-Real-IP")), "-"));
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    private void processTokenAuthentication(Optional<String> token) {
        SecurityContextHolder.getContext().setAuthentication(tryToAuthenticateWithToken(token));
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        return tryToAuthenticate(new PreAuthenticatedAuthenticationToken(token, null));
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        return responseAuthentication;
    }

    /**
     * 处理授权失败
     */
    public static void handleUnAuthentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException {
        logger.error("handleUnAuthentication", authException);

        if(HttpUtils.isAjaxRequest(httpRequest)) {
            String tokenJsonResponse = new ObjectMapper().writeValueAsString(Result.errorResult(HttpResultInfoEnum.UNAUTHORIZED.getCode(), authException.getMessage()));
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpResponse.getWriter().print(tokenJsonResponse);
        }else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

    /**
     * 处理授权内部失败
     */
    public static void handleAuthenticationServiceException(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationServiceException exception) throws IOException {
        logger.error("Internal authentication service exception", exception);

        if(HttpUtils.isAjaxRequest(httpRequest)) {
            String tokenJsonResponse = new ObjectMapper().writeValueAsString(Result.errorResult(HttpResultInfoEnum.SERVER_ERROR.getCode(), HttpResultInfoEnum.SERVER_ERROR.getMessage()+"，"+exception.getMessage()));
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpResponse.getWriter().print(tokenJsonResponse);
        }else {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
