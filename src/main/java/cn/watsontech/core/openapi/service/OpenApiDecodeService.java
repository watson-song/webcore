package cn.watsontech.core.openapi.service;

import cn.watsontech.core.openapi.error.OpenApiResponseErrorHandler;
import cn.watsontech.core.utils.MapBuilder;
import cn.watsontech.core.utils.MapUrlParamsUtils;
import cn.watsontech.core.utils.Md5Util;
import cn.watsontech.core.vo.WxAuthorizeUserVo;
import cn.watsontech.core.web.result.Result;
import cn.watsontech.core.web.spring.util.CollectionUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.RandomStringUtils;
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

    RestTemplate restTemplate;
    String host;

    //微信小程序登录code换openid接口地址
    static final String WXAPP_LOGIN_URL = "/api/open/v1/%s/wxapp/login";
    static final String WXAPP_PARSE_USERINFO_URL = "/api/open/v1/%s/wxapp/parseWxUserInfo";
    static final String WXAPP_SECCHECK_TEXT_URL = "/api/open/v1/%s/secCheck/text";

    static final String WXPUB_AUTHORIZATION_URL = "/api/open/v1/%s/wxpub/buildAuthorizationUrl";//获取授权验证url
    static final String WXPUB_JSTICKETS_URL = "/api/open/v1/%s/wxpub/jsTickets";//获取jsticket
    static final String WXPUB_AUTHCODE_TO_USERINFO_URL = "/api/open/v1/%s/wxpub/getWxAuthorizeUserInfo";//authCode换取userInf


    public OpenApiDecodeService(RestTemplate restTemplate, String host) {
        ConvertUtils.register(new DateLocaleConverter(), Date.class);

        this.restTemplate = restTemplate;
        this.host = host;
        Assert.notNull(host, "开放接口地址不能为空");
    }

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
    }

    /**
     * 示例接口返回包装类
     */
    @Data
    public static class WxAppLoginResponse {
        String openid;
        String sessionKey;
        String unionid;
        String tag = "来自loginResponse";
    }

    /**
     * 示例接口调用 小程序登录code换取openid
     *
     * @param wxAppId 登录appid
     * @param code    登录code
     * @return openid/sessionKey/unionid
     */
    private WxAppLoginResponse wxAppLogin(String appid, String appSecret, String wxAppId, String code) {
        String requestUrl = String.format(WXAPP_LOGIN_URL, appid);
        WxAppLoginForm form = new WxAppLoginForm(wxAppId, code);
        String query = MapUrlParamsUtils.getUrlParamsByMap(openApiParams(appid, appSecret, objectToMap(form), requestUrl));
        return postForResult(requestUrl, query, form, WxAppLoginResponse.class);
    }

    @Data
    public class WxPubBuildAuthorizationUrlForm {
        String wxAppid;
        String redirectURI;
        String scope;
        String state;

        public WxPubBuildAuthorizationUrlForm(String wxAppid, String redirectURI, String scope, String state) {
            this.wxAppid = wxAppid;
            this.redirectURI = redirectURI;
            this.scope = scope;
            this.state = state;
        }
    }

    @Data
    public class WxPubGetJsTicketForm {
        String wxAppid;
        String requestUrl;

        public WxPubGetJsTicketForm(String wxAppid, String requestUrl) {
            this.wxAppid = wxAppid;
            this.requestUrl = requestUrl;
        }
    }

    @Data
    public class WxPubExchangeAuthCodeToUserForm {
        String wxAppid;
        String code;

        public WxPubExchangeAuthCodeToUserForm(String wxAppid, String code) {
            this.wxAppid = wxAppid;
            this.code = code;
        }
    }

    @Data
    public static class WxPubSuccessResponse {
        String success;
    }

    /**
     * 公众号获取授权验证url
     */
    public WxPubSuccessResponse wxpubBuildAuthorizationUrl(String appid, String appSecret, String wxAppId, String redirectURI, String scope, String state) {
        String requestUrl = String.format(WXPUB_AUTHORIZATION_URL, appid);
        WxPubBuildAuthorizationUrlForm form = new WxPubBuildAuthorizationUrlForm(wxAppId, redirectURI, scope, state);
        String query = MapUrlParamsUtils.getUrlParamsByMap(openApiParams(appid, appSecret, objectToMap(form), requestUrl));
        return postForResult(requestUrl, query, form, WxPubSuccessResponse.class);
    }

    /**
     * 公众号获取授权验证url
     */
    public Map wxpubGetJsTickets(String appid, String appSecret, String wxAppId, String url) {
        String requestUrl = String.format(WXPUB_JSTICKETS_URL, appid);
        WxPubGetJsTicketForm form = new WxPubGetJsTicketForm(wxAppId, url);
        String query = MapUrlParamsUtils.getUrlParamsByMap(openApiParams(appid, appSecret, objectToMap(form), requestUrl));
        return postForResult(requestUrl, query, form, Map.class);
    }

    /**
     * 公众号授权码换取用户信息
     */
    public WxAuthorizeUserVo wxpubExchangeAuthCodeToUserInfo(String appid, String appSecret, String wxAppId, String code) {
        String requestUrl = String.format(WXPUB_AUTHCODE_TO_USERINFO_URL, appid);
        WxPubExchangeAuthCodeToUserForm form = new WxPubExchangeAuthCodeToUserForm(wxAppId, code);
        String query = MapUrlParamsUtils.getUrlParamsByMap(openApiParams(appid, appSecret, objectToMap(form), requestUrl));
        return postForResult(requestUrl, query, form, WxAuthorizeUserVo.class);
    }

    protected <T> T postForResult(String requestUrl, String query, Object requestData, Class<T> responseType) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            Result result = restTemplate.postForObject(getHostRequestUrl(requestUrl, query), requestData, Result.class);
            if(result.getData()!=null) {
                if (result.getData() instanceof Map) {
                    return mapToObject((Map<String, Object>)result.getData(), responseType);
                }else {
                    return (T)result.getData();
                }
            }
        }catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    protected <T> T getForResult(String requestUrl, String query, Class<T> responseType) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            Result result = restTemplate.getForObject(getHostRequestUrl(requestUrl, query), Result.class);
            if(result.getData()!=null) {
                if (result.getData() instanceof Map) {
                    return mapToObject((Map<String, Object>)result.getData(), responseType);
                }else {
                    return (T)result.getData();
                }
            }
        }catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    protected void putForResult(String requestUrl, String query, Object requestData) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            restTemplate.put(getHostRequestUrl(requestUrl, query), requestData);
        }catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    protected void deleteForResult(String requestUrl, String query) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            restTemplate.delete(getHostRequestUrl(requestUrl, query));
        }catch (RestClientException ex) {
            ex.printStackTrace();
            message = ex.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }

        throw new HttpServerErrorException(statusCode, message);
    }

    private String getHostRequestUrl(String url, String query) {
        return host + url + "?" + query;
    }

    private Map<String, Object> openApiParams(String appid, String appSecret, Map<Object, Object> queryParams, String requestUrl) {
        Map<String, Object> willSignParams = MapBuilder.builder().putNext("appid", appid).putNext("timestamp", System.currentTimeMillis()).putNext("nonce", RandomStringUtils.randomAlphabetic(5));
        if (queryParams==null) {
            queryParams = new HashMap<>();
        }
        queryParams.putAll(willSignParams);

        willSignParams.put("sign", signParams(queryParams, requestUrl, appSecret));
        return willSignParams;
    }

    private String signParams(Map<Object, Object> apiParams, String requestUrl, String appSecret) {
        String needSignParamString = getNeedSignParamString(apiParams);
        Assert.isTrue(needSignParamString!=null&&!needSignParamString.equals(""), "请求签名参数列表为空");

        return Md5Util.MD5Encode(String.format("%s&appSecret=%s&url=%s", needSignParamString, appSecret, requestUrl)).toUpperCase();
    }

    private String getNeedSignParamString(Map<Object, ?> paramMap) {

        List<Object> fields = new ArrayList<>(paramMap.keySet());
        fields.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(fields)) {
            Object field;
            Object fieldValue;
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                fieldValue = paramMap.get(field);
                if (fieldValue!=null) {
                    if (i>0) {
                        sb.append("&");
                    }
                    sb.append(field).append("=").append(fieldValue);
                }
            }
        }

        return sb.toString();
    }

    public <E> E mapToObject(Map<String, Object> map, Class<E> beanClass) throws Exception {
        if (map == null) return null;
        if (beanClass == Map.class) return (E)map;

        E obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    public Map<Object, Object> objectToMap(Object obj) {
        if(obj == null)
            return null;

        return new org.apache.commons.beanutils.BeanMap(obj);
    }

}