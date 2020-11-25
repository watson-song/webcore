package cn.watsontech.webhelper;

/**
 * Copyright to watsontech
 * Created by watson on 2019/12/22.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信服务 properties
 *
 * @author Watson Song
 */
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
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

    public Map<String, TemplateConfig> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, TemplateConfig> templates) {
        this.templates = templates;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public int getExpireTimeSeconds() {
        return expireTimeSeconds;
    }

    public void setExpireTimeSeconds(int expireTimeSeconds) {
        this.expireTimeSeconds = expireTimeSeconds;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}