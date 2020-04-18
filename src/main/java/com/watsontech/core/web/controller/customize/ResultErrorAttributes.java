package com.watsontech.core.web.controller.customize;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Watson on 2019/12/25.
 */
@Component
public class ResultErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
//        Map<String, Object> errorMap = super.getErrorAttributes(webRequest, includeStackTrace);
        Object message = getAttribute(webRequest, "javax.servlet.error.message");
        Integer status = getAttribute(webRequest, "javax.servlet.error.status_code");
        Throwable error = getError(webRequest);
        if (error!=null) {
            message = error.getMessage();
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("error", StringUtils.isEmpty(message) ? "No message available" : message);
        errors.put("code", status);
        addPath(errors, webRequest);
        return errors;
    }

    private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
