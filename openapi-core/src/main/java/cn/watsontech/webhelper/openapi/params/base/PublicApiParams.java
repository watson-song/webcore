package cn.watsontech.webhelper.openapi.params.base;

import java.util.List;

/**
 * 客户端签名：
 *
 * 生成当前时间戳timestamp=now和唯一随机字符串nonce=random
 * 按照请求参数名的字母升序排列非空请求参数（包含Appid），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
 * stringA="Appid=access&home=world&name=hello&work=java&timestamp=now&nonce=random";
 * 拼接密钥AppSecret
 * stringSignTemp="appid=appid&home=world&name=hello&work=java&timestamp=now&nonce=random&appSecret=secret";
 * 拼接url(requestUrl 为访问路径，除域名外的请求路径，不包含url参数)
 * stringSignTemp="appid=appid&home=world&name=hello&work=java&timestamp=now&nonce=random&appSecret=secret&url=requestUrl";
 * MD5并转换为大写
 * sign=MD5(stringSignTemp).toUpperCase();
 * 最终请求
 * http://api.test.com/test?appid=appid&name=hello&home=world&work=java&timestamp=now&nonce=nonce&sign=sign;
 *
 * 服务器验证：
 * [!image](https://upload-images.jianshu.io/upload_images/10418241-7643ad86154b365d.png?imageMogr2/auto-orient/strip|imageView2/2/w/338/format/webp)
 *
 * Created by Watson on 2020/02/09.
 */
public interface PublicApiParams {

    /**
     * 服务器分配的appid
     */
    String getAppid();

    /**
     * 请求时间戳
     * 值必须在服务器允许时间范围内，比如：60s
     */
    Long getTimestamp();

    /**
     * 1、按照请求参数名的字母升序排列非空请求参数（包含&appid=appid)
     * 2、拼接密钥appSecret：&appSecret=secret
     * 3、拼接url：&url=请求路径（除域名和url参数外部分，比如：http://www.xxx.com/api/v1/sms/list?paramA=1&paramB=2 的url为 /api/v1/sms/list）
     * 3、MD5并转换为大写
     */
    String getSign();

    /**
     * 唯一随机字符串nonce=random
     * 服务器保存每个请求nonce直到timestamp过期
     * 防请求重放
     */
    String getNonce();

    /**
     * @param extraApiParams 需要参与签名的额外参数对象
     * 返回按参数名字母升序排列的非空请求参数拼接字符串
     */
    String getNeedSignParamString(List<OpenApiParams> extraApiParams);

    /**
     * 请求id
     */
    String getRequestId();

    void setRequestId(String requestId);
}
