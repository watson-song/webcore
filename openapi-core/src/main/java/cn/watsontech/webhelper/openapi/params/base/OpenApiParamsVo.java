package cn.watsontech.webhelper.openapi.params.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 注意：不能使用此vo直接做请求参数，缺少具体参数
 * Created by Watson on 2020/02/09.
 */
public class OpenApiParamsVo implements PublicApiParams {
    @IgnoreField
    static final Log log = LogFactory.getLog(OpenApiParamsVo.class);

    @ApiModelProperty(name = "appid")
    @NotNull(message = "appid不能为空")
    String appid;

    @ApiModelProperty(name = "timestamp", notes = "时间戳")
    @NotNull(message = "时间戳不能为空")
    Long timestamp;

    @ApiModelProperty(name = "sign", notes = "签名")
    @NotNull(message = "签名不能为空")
    @IgnoreField
    String sign;

    @ApiModelProperty(name = "随机串", notes = "随机字符串")
    @NotNull(message = "nonce不能为空")
    String nonce;

    @IgnoreField
    String requestId;

    @Override
    public String getNeedSignParamString(Collection<OpenApiParams> extraApiParams) {
        Map<String, String> fieldMap = new HashMap<>();

        //1、首先获取本类和子类下的所有fields
        try {
            List<Field> fields = getChildNotNullFields(getClass(), this);
            //添加所有fields到map中
            addAllFieldToMap(fields, this, fieldMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error("加密字符串，field不能访问："+e.getMessage(), e);
        }

        //2、其次获取extraApiParams下的所有fields
        if(!CollectionUtils.isEmpty(extraApiParams)) {
            extraApiParams.forEach(extraApiParam -> {
                if (extraApiParam instanceof MapOpenApiParams) {
                    //map参数不能有重复，重复会覆盖
                    ((MapOpenApiParams<String, Object>)extraApiParam).forEach((extraApiParamKey, extraApiParamValue) -> {
                        fieldMap.put(extraApiParamKey, parseObject(extraApiParamValue));
                    });
                }else {
                    try {
                        List<Field> extraFields = getChildNotNullFields(extraApiParam.getClass(), extraApiParam);
                        //添加所有fields到map中
                        addAllFieldToMap(extraFields, extraApiParam, fieldMap);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        log.error("加密字符串序列化extraFields，转换额外参数parseObject失败："+extraApiParam.getClass(), e);
                    }
                }
            });
        }

        List<String> keys = new ArrayList<>(fieldMap.keySet());
        keys.sort((k1, k2) -> k1.compareToIgnoreCase(k2));

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(keys)) {
            String field;
            for (String key:keys) {
                field = fieldMap.get(key);
                if (field!=null) {
                    sb.append(key).append("=").append(field).append("&");
                }
            }

            //删除末尾的 &
            sb.setLength(sb.length()-1);
        }

        return sb.toString();
    }

    private String getFieldValue(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = field.get(object);

        return parseObject(fieldValue);
    }

    private String parseObject(Object value) {
        if (value==null) return "";

        if (value instanceof CharSequence) {
            return value.toString();
        }else if (value instanceof Boolean) {
            return value.toString();
        }else if (value instanceof Number) {
            return value.toString();
        }

        return JSON.toJSONString(value);
    }

    List<Field> getChildNotNullFields(Class claz, Object object) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>();
        if (claz!=null&&claz!=Object.class) {
            fields = getNotNullFields(claz, object);

            Class supClaz = claz.getSuperclass();
            if (supClaz!=null) {
                fields.addAll(getChildNotNullFields(supClaz, object));
            }
        }
        return fields;
    }

    protected List<Field> getNotNullFields(Class thisClaz, Object object) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>();
        Field[] allFields = thisClaz.getDeclaredFields();
        for (Field field:allFields) {

            IgnoreField ignoreField = field.getAnnotation(IgnoreField.class);
            if (ignoreField==null) {
                //Bugfix for cannot access with modifiers ""
                field.setAccessible(true);
                if(field.get(object)!=null) {
                    fields.add(field);
                }
            }

        }
        return fields;
    }

    private void addAllFieldToMap(List<Field> fields, Object object, Map<String, String> map) throws IllegalAccessException {
        if (fields!=null) {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                map.put(field.getName(), getFieldValue(field, object));
            }
        }
    }

    @Override
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * 根据内容生成openapi签名链接
     */
    public String toUrl() {
        return String.format("appid=%s&sign=%s&nonce=%s&timestamp=%s", appid, sign, nonce, timestamp);
    }

    /**
     * 根据内容生成openapi签名链接
     */
    public String toGetUrl() {
        return String.format("appid=%s&sign=%s&nonce=%s&timestamp=%s", appid, sign, nonce, timestamp);
    }
}
