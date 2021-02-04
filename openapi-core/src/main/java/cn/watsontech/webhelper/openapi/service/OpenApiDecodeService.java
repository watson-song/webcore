package cn.watsontech.webhelper.openapi.service;

import cn.watsontech.webhelper.common.result.Result;
import cn.watsontech.webhelper.common.vo.WxAuthorizeUserVo;
import cn.watsontech.webhelper.openapi.error.OpenApiResponseErrorHandler;
import cn.watsontech.webhelper.openapi.form.wx.WxMaSubscribeMsgForm;
import cn.watsontech.webhelper.openapi.form.wx.WxMessageData;
import cn.watsontech.webhelper.openapi.form.wx.WxMpSubscribeMsgForm;
import cn.watsontech.webhelper.openapi.params.base.MapOpenApiParams;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParamsVo;
import cn.watsontech.webhelper.utils.Md5Util;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by Watson on 2020/10/19.
 */
public class OpenApiDecodeService {

    RestTemplate restTemplate;
    String host;

    //微信小程序登录code换openid接口地址
    static final String WXAPP_LOGIN_URL = "/api/open/v1/%s/wxapp/login";
    static final String WXAPP_PARSE_USERINFO_URL = "/api/open/v1/%s/wxapp/parseWxUserInfo";
    static final String WXAPP_SECCHECK_TEXT_URL = "/api/open/v1/%s/secCheck/text";
    static final String WXAPP_SEND_SUBSCRIBE_MESSAGE = "/api/open/v1/%s/wxapp/sendMaSubscribeMsg";//POST 发送小程序订阅消息

    static final String FILE_BATCH_UPLOAD_URL = "/api/open/v1/%s/files/batch";

    static final String WXPUB_AUTHORIZATION_URL = "/api/open/v1/%s/wxpub/buildAuthorizationUrl";//获取授权验证url
    static final String WXPUB_JSTICKETS_URL = "/api/open/v1/%s/wxpub/jsTickets";//获取jsticket
    static final String WXPUB_AUTHCODE_TO_USERINFO_URL = "/api/open/v1/%s/wxpub/getWxAuthorizeUserInfo";//authCode换取userInf
    static final String WXPUB_SEND_SUBSCRIBE_MESSAGE = "/api/open/v1/%s/wxpub/sendMpSubscribeMsg";//POST 发送公众号订阅消息


    public OpenApiDecodeService(RestTemplate restTemplate, String host) {
        ConvertUtils.register(new DateLocaleConverter(), Date.class);

        this.restTemplate = restTemplate;
        this.host = host;
        Assert.notNull(host, "开放接口地址不能为空");
    }

    /**
     * 示例参数
     */
    public class WxAppLoginForm {
        String wxAppid;
        String code;

        public WxAppLoginForm(String wxAppId, String code) {
            this.wxAppid = wxAppId;
            this.code = code;
        }

        public String getWxAppid() {
            return wxAppid;
        }

        public void setWxAppid(String wxAppid) {
            this.wxAppid = wxAppid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * 示例接口返回包装类
     */
    public static class WxAppLoginResponse {
        String openid;
        String sessionKey;
        String unionid;
        String tag = "来自loginResponse";

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSessionKey() {
            return sessionKey;
        }

        public void setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    /**
     * 示例接口调用 小程序登录code换取openid
     *
     * @param wxAppId 登录appid
     * @param code    登录code
     * @return openid/sessionKey/unionid
     */
    public WxAppLoginResponse wxAppLogin(String appid, String appSecret, String wxAppId, String code) {
        String requestUrl = String.format(WXAPP_LOGIN_URL, appid);
        WxAppLoginForm form = new WxAppLoginForm(wxAppId, code);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, WxAppLoginResponse.class);
    }

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

        public String getWxAppid() {
            return wxAppid;
        }

        public void setWxAppid(String wxAppid) {
            this.wxAppid = wxAppid;
        }

        public String getRedirectURI() {
            return redirectURI;
        }

        public void setRedirectURI(String redirectURI) {
            this.redirectURI = redirectURI;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public class WxPubGetJsTicketForm {
        String wxAppid;
        String requestUrl;

        public WxPubGetJsTicketForm(String wxAppid, String requestUrl) {
            this.wxAppid = wxAppid;
            this.requestUrl = requestUrl;
        }

        public String getWxAppid() {
            return wxAppid;
        }

        public void setWxAppid(String wxAppid) {
            this.wxAppid = wxAppid;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }
    }

    public class WxPubExchangeAuthCodeToUserForm {
        String wxAppid;
        String code;

        public WxPubExchangeAuthCodeToUserForm(String wxAppid, String code) {
            this.wxAppid = wxAppid;
            this.code = code;
        }

        public String getWxAppid() {
            return wxAppid;
        }

        public void setWxAppid(String wxAppid) {
            this.wxAppid = wxAppid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class WxPubSuccessResponse {
        String success;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }
    }

    /**
     * 公众号获取授权验证url
     */
    public WxPubSuccessResponse wxpubBuildAuthorizationUrl(String appid, String appSecret, String wxAppId, String redirectURI, String scope, String state) {
        String requestUrl = String.format(WXPUB_AUTHORIZATION_URL, appid);
        WxPubBuildAuthorizationUrlForm form = new WxPubBuildAuthorizationUrlForm(wxAppId, redirectURI, scope, state);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, WxPubSuccessResponse.class);
    }

    /**
     * 公众号获取授权验证url
     */
    public Map wxpubGetJsTickets(String appid, String appSecret, String wxAppId, String url) {
        String requestUrl = String.format(WXPUB_JSTICKETS_URL, appid);
        WxPubGetJsTicketForm form = new WxPubGetJsTicketForm(wxAppId, url);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, Map.class);
    }

    /**
     * 公众号授权码换取用户信息
     */
    public WxAuthorizeUserVo wxpubExchangeAuthCodeToUserInfo(String appid, String appSecret, String wxAppId, String code) {
        String requestUrl = String.format(WXPUB_AUTHCODE_TO_USERINFO_URL, appid);
        WxPubExchangeAuthCodeToUserForm form = new WxPubExchangeAuthCodeToUserForm(wxAppId, code);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, WxAuthorizeUserVo.class);
    }

    public static class BaseSuccessResponse<T> {
        T success;

        public T getSuccess() {
            return success;
        }

        public void setSuccess(T success) {
            this.success = success;
        }
    }

    /**
     * 发送小程序订阅消息
     */
    public BaseSuccessResponse<Integer> sendWxMaSubscribeMessage(String appid, String appSecret, String wxAppId, String templateId, String toUser, String page, List<WxMessageData> data) {
        String requestUrl = String.format(WXAPP_SEND_SUBSCRIBE_MESSAGE, appid);
        //String wxAppid, String templateId, String toUser, String page, List<WxMessageData> data
        WxMaSubscribeMsgForm form = new WxMaSubscribeMsgForm(wxAppId, templateId, toUser, page, data);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, BaseSuccessResponse.class);
    }

    /**
     * 发送公众号订阅消息
     */
    public BaseSuccessResponse<Boolean> sendWxMpSubscribeMessage(String appid, String appSecret, String wxAppId, String templateId, String toUser, String url, String scene, String title, String contentValue, String contentColor, WxMpSubscribeMsgForm.MiniApp miniProgram) {
        String requestUrl = String.format(WXPUB_SEND_SUBSCRIBE_MESSAGE, appid);
        //String wxAppid, String templateId, String toUser, String url, String scene, String title, String contentValue, String contentColor
        WxMpSubscribeMsgForm form = new WxMpSubscribeMsgForm(wxAppId, templateId, toUser, url, scene, title, contentValue, contentColor);
        form.setMiniProgram(miniProgram);
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();
        return postForResult(requestUrl, query, form, BaseSuccessResponse.class);
    }

    protected <T> T postFilesForResult(String requestUrl, String query, Object requestData, Class<T> responseType) {
        String message = "未知错误";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            restTemplate.setErrorHandler(new OpenApiResponseErrorHandler());
            ResponseEntity<Result> resultResponseEntity = restTemplate.postForEntity(getHostRequestUrl(requestUrl, query), requestData, Result.class);
            if (resultResponseEntity!=null) {
                Result result = resultResponseEntity.getBody();
                if (result!=null) {
                    if(result.getData()!=null) {
                        if (result.getData() instanceof Map) {
                            return mapToObject((Map<String, Object>)result.getData(), responseType);
                        }else {
                            return (T)result.getData();
                        }
                    }
                }

                statusCode = resultResponseEntity.getStatusCode();
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

    protected String getHostRequestUrl(String url, String query) {
        return host + url + "?" + query;
    }

    protected OpenApiParamsVo openApiParams(String appid, String appSecret, OpenApiParams queryParam, String requestUrl) {
        return openApiParams(appid, appSecret, Arrays.asList(queryParam), requestUrl);
    }

    protected OpenApiParamsVo openApiParams(String appid, String appSecret, List<OpenApiParams> queryParams, String requestUrl) {
        OpenApiParamsVo openApiParamsVo = new OpenApiParamsVo();
        openApiParamsVo.setAppid(appid);
        openApiParamsVo.setTimestamp(System.currentTimeMillis());
        openApiParamsVo.setNonce(RandomStringUtils.randomAlphabetic(5));
        String needSignParamString = openApiParamsVo.getNeedSignParamString(queryParams);

        openApiParamsVo.setSign(signParams(needSignParamString, requestUrl, appSecret));
        return openApiParamsVo;
    }

    protected String signParams(String needSignParamString, String requestUrl, String appSecret) {
//        String needSignParamString = getNeedSignParamString(apiParams);
        Assert.isTrue(needSignParamString!=null&&!needSignParamString.equals(""), "请求签名参数列表为空");

        return Md5Util.MD5Encode(String.format("%s&appSecret=%s&url=%s", needSignParamString, appSecret, requestUrl)).toUpperCase();
    }

    protected String getNeedSignParamString(Map<String, ?> paramMap) {

        List<String> fields = new ArrayList<>(paramMap.keySet());
        fields.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(fields)) {
            String field;
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

    protected <E> E mapToObject(Map<String, Object> map, Class<E> beanClass) throws Exception {
        if (map == null) return null;
        if (beanClass == Map.class) return (E)map;

        E obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    protected MapOpenApiParams<String, Object> objectToMap(Object obj) {
        if(obj == null)
            return null;

        Map<Object, Object> beanMap= new org.apache.commons.beanutils.BeanMap(obj);
        MapOpenApiParams<String, Object> result = new MapOpenApiParams<>();
        //移除class并重新装map，beanmap不能put
        for (Object keyObject:beanMap.keySet()) {
            String key = keyObject.toString();
            if (!"class".equalsIgnoreCase(key)) {
                result.put(key, beanMap.get(key));
            }
        }

        return result;
    }

}