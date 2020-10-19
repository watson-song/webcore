package cn.watsontech.core.openapi.service;

import cn.watsontech.core.openapi.error.OpenApiResponseErrorHandler;
import cn.watsontech.core.utils.MapBuilder;
import cn.watsontech.core.utils.MapUrlParamsUtils;
import cn.watsontech.core.utils.Md5Util;
import cn.watsontech.core.web.result.Result;
import cn.watsontech.core.web.spring.util.CollectionUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by Watson on 2020/10/19.
 */
@Log4j2
public class OpenApiDecodeService {

    @Autowired
    protected RestTemplate restTemplate;

    /**
     * 示例参数
     */
    @Data
    public class WxAppLoginForm {
        String wxAppid;
        String code;

        public WxAppLoginForm(String wxAppId, String code) {
            this.wxAppid = wxAppId;
            this.code = code;
        }

        public Map<String, Object> toQueryMap() {
            MapBuilder mapBuilder = MapBuilder.builder();
            if (code != null) {
                mapBuilder.putNext("code", code);
            }
            if (wxAppid != null) {
                mapBuilder.putNext("wxAppid", wxAppid);
            }
            return mapBuilder;
        }
    }

    /**
     * 示例接口返回包装类
     */
    @Data
    public static class WxAppLoginResponse implements MapResponse<WxAppLoginResponse> {
        String openid;
        String sessionKey;
        String unionid;
        String tag = "来自loginResponse";

        @Override
        public WxAppLoginResponse initWithMap(Map<String, Object> data) {
            if (data != null) {
                openid = (String) data.get("openid");
                sessionKey = (String) data.get("sessionKey");
                unionid = (String) data.get("unionid");
            }

            return this;
        }
    }

    public interface MapResponse<T> {
        T initWithMap(Map<String, Object> data);
    }

    /**
     * 示例接口调用 小程序登录code换取openid
     *
     * @param wxAppId 登录appid
     * @param code    登录code
     * @return openid/sessionKey/unionid
     */
    private WxAppLoginResponse wxAppLogin(String requestUrl, String appid, String appSecret, String wxAppId, String code) {
        WxAppLoginForm form = new WxAppLoginForm(wxAppId, code);
        String query = MapUrlParamsUtils.getUrlParamsByMap(openApiParams(appid, appSecret, form.toQueryMap(), requestUrl));
//GET请求：return getForResult(requestUrl, query+"&"+MapUrlParamsUtils.getUrlParamsByMap(form.toQueryMap()), WxAppTextSecCheckResponse.class);
        return postForResult(requestUrl, query, form, WxAppLoginResponse.class);
    }

    protected <T extends MapResponse> T postForResult(String requestUrl, String query, Object requestData, Class<T> responseType) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            String fullRequestUrl = getHostRequestUrl(requestUrl, query);
            log.debug("发起远程调用请求 {}", fullRequestUrl);
            Result result = restTemplate.postForObject(fullRequestUrl, requestData, Result.class);
            log.debug("收到程调用响应 {}", result);

            if (result.getData() != null) {
                return (T) responseType.newInstance().initWithMap((Map) result.getData());
            }
        } catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    protected <T extends MapResponse> T getForResult(String requestUrl, String query, Class<T> responseType) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            String fullRequestUrl = getHostRequestUrl(requestUrl, query);
            log.debug("发起远程调用请求 {}", fullRequestUrl);
            Result result = restTemplate.getForObject(fullRequestUrl, Result.class);
            log.debug("收到程调用响应 {}", result);

            if (result.getData() != null) {
                return (T) responseType.newInstance().initWithMap((Map) result.getData());
            }
        } catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    private String getHostRequestUrl(String url, String query) {
        return url + "?" + query;
    }

    protected Map<String, Object> openApiParams(String appid, String appSecret, Map<String, Object> queryParams, String requestUrl) {
        Map<String, Object> willSignParams = MapBuilder.builder().putNext("appid", appid).putNext("timestamp", System.currentTimeMillis()).putNext("nonce", RandomStringUtils.randomAlphabetic(5));
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }
        queryParams.putAll(willSignParams);

        willSignParams.put("sign", signParams(appSecret, queryParams, requestUrl));
        return willSignParams;
    }

    private String signParams(String appSecret, Map<String, Object> apiParams, String requestUrl) {
        String needSignParamString = getNeedSignParamString(apiParams);
        Assert.isTrue(needSignParamString != null && !needSignParamString.equals(""), "请求签名参数列表为空");

        return Md5Util.MD5Encode(String.format("%s&appSecret=%s&url=%s", needSignParamString, appSecret, requestUrl)).toUpperCase();
    }

    private String getNeedSignParamString(Map<String, Object> paramMap) {
        List<String> fields = new ArrayList<>(paramMap.keySet());
        fields.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(fields)) {
            String field;
            Object fieldValue;
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                fieldValue = paramMap.get(field);
                if (fieldValue != null) {
                    if (i > 0) {
                        sb.append("&");
                    }
                    sb.append(field).append("=").append(fieldValue);
                }
            }
        }

        return sb.toString();
    }

}