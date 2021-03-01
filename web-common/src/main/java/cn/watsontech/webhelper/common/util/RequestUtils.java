package cn.watsontech.webhelper.common.util;

import cn.watsontech.webhelper.utils.StringUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
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
     * 获得用户ip,通过nginx代理过来，如果nginx为二级（上面有slb），那个真实ip就是x-forwarded-for，否则就是 remoteAddr
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
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

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

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

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }
}
