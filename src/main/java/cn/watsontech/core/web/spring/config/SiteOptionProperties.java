package cn.watsontech.core.web.spring.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/27.
 */
@Data
@Log4j2
@ConfigurationProperties(prefix = "site.options")
public class SiteOptionProperties {
    //common 配置
    public static final String TokenExpireInMinutes_Key = "tokenExpire.inMinutes"; //客户端令牌(Token)过期时间(分钟)
    public static final String RefreshTokenExpireInMonth_Key = "refreshTokenExpire.inMonth"; //客户端刷新令牌(RefreshToken)过期时间(月)

    //bankend 配置
    public static final String OrderExpireInMinutes_Key = "orderExpire.inMinutes"; //Bankend 订单待支付过期时间(分钟)
    public static final String OrderPlatformFeePercentage_Key = "orderPlatformFee.percentage"; //Bankend 平台服务费百分比

    @Autowired
    JdbcTemplate jdbcTemplate;
    Map<String, Map<String, Object>> keyValues;

    @PostConstruct
    public void init() {
        reloadFromStorage();
    }

    /**
     * 重新从数据库加载配置
     */
    @Scheduled(initialDelay=1*60*1000, fixedDelay=1*60*1000)//5*60s重新加载
    public void reloadFromStorage() {
        if(CollectionUtils.isEmpty(keyValues)) {
            keyValues = new HashMap<>();
        }

        List<Map<String, Object>> siteOptions = jdbcTemplate.queryForList("select name, type, _key, value, enabled from tb_site_option ");// where enabled = 1

        if (!CollectionUtils.isEmpty(siteOptions)) {
            List<String> disabledKeys = siteOptions.stream().filter(option -> (Boolean)option.getOrDefault("enabled", false)).map(option -> (String)option.getOrDefault("_key", "unknowKey")).collect(Collectors.toList());
            //1、先删除已禁用key
            if (!CollectionUtils.isEmpty(disabledKeys)) {
                disabledKeys.forEach(disabledKey -> keyValues.remove(disabledKey));
            }

            //2、后添加已启用配置
            Map<String, Map<String, Object>> newSiteOptions = siteOptions.stream().filter(option -> (Boolean)option.getOrDefault("enabled", 1)).collect(Collectors.toMap(option -> option.getOrDefault("type", "unknowType")+"."+option.getOrDefault("_key", "unknowKey"), option -> option));//.filter(option -> option.get("_key")!=null&&option.get("value")!=null)
            keyValues.putAll(newSiteOptions);
        }
    }

    /**
     * 获取客户端相关关键字的配置，未设置则返回null
     * @param type 客户端
     * @param key 关键字key
     */
    public String getValue(String type, String key) {
        String result = null;
        if (keyValues!=null) {
            Map<String, Object> valueMap = keyValues.getOrDefault(type+"."+key, new HashMap<>());
            result = (String)valueMap.get("value");
        }

        return result;
    }

    /**
     * 获取客户端相关关键字的配置，未设置则返回默认值
     * @param type 客户端
     * @param key 关键字key
     * @param defaultValue 默认值
     */
    public String getValue(String type, String key, String defaultValue) {
        String result = null;
        if (keyValues!=null) {
            Map<String, Object> valueMap = keyValues.getOrDefault(type+"."+key, new HashMap<>());
            result = (String)valueMap.get("value");
        }

        if (result==null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 获取某客户端某key的相关整数配置
     * @param type 客户端
     * @param key 关键key
     * @param defaultValue 默认值
     */
    public int getIntValue(String type, String key, Integer defaultValue) {
        String result = getValue(type, key);
        if (result!=null) {
            try {
                return Integer.parseInt(result);
            }catch (NumberFormatException ex) {ex.printStackTrace();}
        }

        return defaultValue!=null?defaultValue:0;
    }

    /**
     * 获取客户端所有相关关键字的配置
     * @param type 客户端
     */
    public Object getTypeValues(String type) {
        Map<String, Object> valueMap = new HashMap<>();
        if (keyValues!=null) {
            List<String> filtedKeys = keyValues.keySet().stream().filter(tmpKey -> tmpKey.startsWith(type+".")).collect(Collectors.toList());

            for (int i = 0; i < filtedKeys.size(); i++) {
                String tmpKey = filtedKeys.get(i);
                valueMap.put(tmpKey, keyValues.get(tmpKey));
            }
        }
        return valueMap;
    }
}