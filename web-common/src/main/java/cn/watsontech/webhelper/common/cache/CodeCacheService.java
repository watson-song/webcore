package cn.watsontech.webhelper.common.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Watson on 2020/3/2.
 */
public class CodeCacheService {

    CacheManager cacheManager;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 存储code和业务+mobile
     * @param code 生成的code
     * @param buzNameAndMobile buzName+":"+mobile
     * @param ttl 过期时间
     */
    public void store(String cacheName, String code, String buzNameAndMobile, int ttl) {
        Element element = new Element(buzNameAndMobile, code);
        element.setTimeToLive(ttl);

        Cache cache = this.cacheManager.getCache(cacheName);
        if (cache!=null) {
            cache.put(element);
        }
    }

    /**
     * 删除code
     * @param buzNameAndMobile 短信code
     * @return 是否删除成功
     */
    public boolean removeCode(String cacheName, String buzNameAndMobile) {
        Cache cache = this.cacheManager.getCache(cacheName);
        if (cache!=null) {
            return buzNameAndMobile!=null?cache.remove(buzNameAndMobile):false;
        }

        return false;
    }

    /**
     * 是否包含code
     * @param buzNameAndMobile 登录code
     * @return 是否包含
     */
    public boolean containsCode(String cacheName, String buzNameAndMobile, String code) {
        Cache cache = this.cacheManager.getCache(cacheName);
        if (cache!=null) {
            Element element = cache.get(buzNameAndMobile);
            if (element!=null) {
                return element.getObjectValue().toString().equals(code);
            }
        }

        return false;
    }

}
