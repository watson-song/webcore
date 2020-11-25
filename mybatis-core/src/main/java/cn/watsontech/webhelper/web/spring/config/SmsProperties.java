package cn.watsontech.webhelper.web.spring.config;

/**
 * Copyright to watsontech
 * Created by watson on 2019/12/22.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信服务 properties
 *
 * @author Watson Song
 */
@Data
@Component
@ConfigurationProperties(prefix = "site.sms")
public class SmsProperties {
    private Map<String, TemplateConfig> templates;

    /**
     * 供应商名称，aliyun\tencent
     */
    String vendor;
    String appid;
    Integer id;
    String regionId;

    /**
     * 签名名称
     */
    String signName;

    /**
     * 过期时间，秒
     */
    int expireTimeSeconds;

    String appKey;
    String appSecret;

    @Data
    public static class TemplateConfig {
        /**
         * 设置模板ID
         */
        private String id;

        /**
         * 模板名称
         */
        private String name;

        /**
         * 设置模板参数，带%s placeholder
         */
        private String params;

    }

    /**
     * 从map加载属性并返回实例
     */
    public static SmsProperties loadFromMap(Map<String, String> valueMap) {
        SmsProperties properties = new SmsProperties();
        properties.setId(Integer.parseInt(valueMap.getOrDefault("id", "0")));
        properties.setAppid(valueMap.get("appid"));
        properties.setVendor(valueMap.get("vendor"));
        properties.setSignName(valueMap.get("signName"));
        properties.setRegionId(valueMap.get("regionId"));
        properties.setAppKey(valueMap.get("appKey"));
        properties.setAppSecret(valueMap.get("appSecret"));
        return properties;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}