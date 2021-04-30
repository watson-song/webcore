package cn.watsontech.openapi;

import cn.watsontech.webhelper.openapi.params.base.MapOpenApiParams;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class UtilApplication {
    static String host = "HOST";
    static String hourly_url = "/public/statistics/flow/hourly";
    static String pvFlow_url = "/public/statistics/flow";
    static String appId = "appid";
    static String appSecret = "secret";

    public static void main(String[] args) {
//        String host = args[0];
//        String url = args[1];
//        String appId = args[2];
//        String appSecret = args[3];
//        String params = args[4];

//        Map formDatas = JSON.parseObject(params, MapOpenApiParams.class);
        OpenApiCallService openApiCallService = new OpenApiCallService(new RestTemplate(), host);
        MapOpenApiParams singMap = new MapOpenApiParams();
//        singMap.put("mallId", 523461890790633473L);
//        singMap.put("date", "2021-04-28");
//        singMap.put("hours", Arrays.asList(0,1,2,3,4,5,6,7,8,9));
//        singMap.put("rewrite", false);

        MapOpenApiParams signParms = new MapOpenApiParams();
        signParms.put("date", "2021-04-28");
        List response = openApiCallService.callRemoteApiGet(appId, appSecret, hourly_url, singMap, signParms, List.class);

        log.info("调用参数，host：{}，调用返回结果：{}", host, response);
        System.out.println("main->"+JSON.toJSON(response));
    }
}
