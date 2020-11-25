package cn.watsontech.webhelper.common.util;

import cn.watsontech.webhelper.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Watson on 2020/02/09.
 */
public class RequestUtils {

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static String getRequestType(HttpServletRequest request) {
        return request.getHeader("X-Requested-With");
    }

    /**
     * 获取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }

        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获取域名
     * @return http://localhost:7443
     */
    public static String getRequestDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        //TODO 请求强制转成https
        String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        domain = domain.replace("http://", "https://");
        return domain;
    }


    public static String getRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            return url +"?"+queryString;
        }

        return url;
    }

    public static String getFullRequestUrl(HttpServletRequest request) {
        //TODO 请求强制转成https
        String url = request.getRequestURL().toString().replace("http://", "https://");
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            return url +"?"+queryString;
        }

        return url;
    }

    public static String addParameterToUrl(String url, String name, Object value) {
        if (value==null) {
            return url;
        }

        if (!StringUtils.isEmpty(url)) {
            if (url.contains("?")) {
              return String.format("%s&%s=%s", url, name, value);
            }
        }

        return String.format("%s?%s=%s", url, name, value);
    }


    static Pattern uaManufacturerPattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/(\\S*)?;\\s");
    static Pattern uaMicromessageSpiderPattern = Pattern.compile("\\s?(MicroMessenger/\\S*\\s?mpcrawler)+");

    /**
     * 解析useragent的android手机厂商
     */
    public static String parseAndroidManufacturer(String userAgent) {
        String model = null;
        Matcher matcher = matchPattern(uaManufacturerPattern, userAgent);
        if (matcher!=null&&matcher.find()) {
            model = matcher.group(1).trim();
            if (matcher.groupCount()>2) {
                model += ","+matcher.group(3).trim();
            }
        }

        return model;
    }

    /**
     * 解析useragent的检查是否是微信官方spider
     */
    public static boolean isMicromessageSpider(String userAgent) {
        Matcher matcher = matchPattern(uaMicromessageSpiderPattern, userAgent);
        return matcher!=null&&matcher.find();
    }

    private static Matcher matchPattern(Pattern pattern, String userAgent) {
        Matcher matcher = null;
        if (!StringUtils.isEmpty(userAgent)) {
            matcher = pattern.matcher(userAgent);
        }

        return matcher;
    }
}
