package cn.watsontech.openapi;

import cn.watsontech.webhelper.openapi.params.base.MapOpenApiParams;
import cn.watsontech.webhelper.openapi.service.OpenApiDecodeService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class OpenApiCallService extends OpenApiDecodeService {

    public OpenApiCallService(RestTemplate restTemplate, String host) {
        super(restTemplate, host);
    }

    public Map callRemoteApiPost(String appid, String appSecret, String requestUrl, Map form) {
        String query = openApiParams(appid, appSecret, objectToMap(form), requestUrl).toUrl();

        log.info("调用远程开放POST接口，appid：{}，地址：{}，参数：{}，", appid, requestUrl, query);
        return postForResult(requestUrl, query, form, Map.class);
    }

    public  <T> T callRemoteApiGet(String appid, String appSecret, String requestUrl, final Map<String, Object> unsignParams, MapOpenApiParams form, Class<T> responseClaz) {
        String query = openApiParams(appid, appSecret, form, requestUrl).toGetUrl();
        List<String> a = unsignParams.keySet().stream().map(paramKey -> (String)(paramKey+"="+unsignParams.get(paramKey))).collect(Collectors.toList());
        query =  query + "&"+StringUtils.join(a, "&");
        query = query.replace("[","").replace("]","");

        log.info("调用远程开放GET接口，appid：{}，地址：{}，参数：{}，", appid, requestUrl, query);
        return getForResult(requestUrl, query, responseClaz);
    }

}
