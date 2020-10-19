package cn.watsontech.core.utils;

/**
 * Created by watson on 2020/6/17.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * map 与 url参数转换
 */

public class MapUrlParamsUtils {

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */

    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        if (org.springframework.util.StringUtils.isEmpty(param)) {
            return map;
        }

        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }

        return map;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */

    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }

        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang3.StringUtils.substringBeforeLast(s, "&");
        }

        return s;
    }

}
