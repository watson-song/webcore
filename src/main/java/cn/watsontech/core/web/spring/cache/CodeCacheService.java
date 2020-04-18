package cn.watsontech.core.web.spring.cache;

import lombok.extern.log4j.Log4j2;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Watson on 2020/3/2.
 */
@Log4j2
@Service
public class CodeCacheService {

    CacheManager cacheManager;
    Cache codeCache;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.codeCache = this.cacheManager.getCache("smsCodeCache");
    }

    /**
     * 存储code和业务+mobile
     * @param code 生成的code
     * @param buzNameAndMobile buzName+":"+mobile
     * @param ttl 过期时间
     */
    public void store(String code, String buzNameAndMobile, int ttl) {
        Element element = new Element(buzNameAndMobile, code);
        element.setTimeToLive(ttl);
        codeCache.put(element);
    }

    /**
     * 删除code
     * @param buzNameAndMobile 短信code
     * @return 是否删除成功
     */
    public boolean removeCode(String buzNameAndMobile) {
        return buzNameAndMobile!=null?codeCache.remove(buzNameAndMobile):false;
    }

    /**
     * 是否包含code
     * @param buzNameAndMobile 登录code
     * @return 是否包含
     */
    public boolean containsCode(String buzNameAndMobile, String code) {
        Element element = codeCache.get(buzNameAndMobile);
        if (element!=null) {
            return element.getObjectValue().toString().equals(code);
        }

        return false;
    }

}
