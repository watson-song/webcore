package com.watsontech.core.utils;

import com.watsontech.core.web.spring.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class HttpUtils {

    public static String generateUrl(String url, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        params.entrySet().forEach(entry -> sb.append("&").append(entry.getKey()).append("=").append(entry.getValue()));
        if (url.indexOf("?") == -1) {
            url = url + "?" + sb.toString().replaceFirst("&", "");
        } else {
            url = url + sb.toString();
        }

        return url;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 获得用户ip,通过nginx代理过来，如果nginx为二级（上面有slb），那个真实ip就是x-forwarded-for，否则就是 remoteAddr
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
    	String realIp = request.getHeader("x-forwarded-for");
    	if(StringUtils.isEmpty(realIp) || "unknown".equalsIgnoreCase(realIp)) {
    		realIp = request.getHeader("x-real-ip");
    	}
    	if(StringUtils.isEmpty(realIp) || "unknown".equalsIgnoreCase(realIp)) {
    		realIp = request.getRemoteAddr();
    	}
    	if(StringUtils.isEmpty(realIp) || "unknown".equalsIgnoreCase(realIp)) {
    		realIp = "127.0.0.1";
    	}
    	return realIp;
    }
}
