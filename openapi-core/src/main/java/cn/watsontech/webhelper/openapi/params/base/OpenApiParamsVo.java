package cn.watsontech.webhelper.openapi.params.base;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 注意：不能使用此vo直接做请求参数，缺少具体参数
 * Created by Watson on 2020/02/09.
 */
public class OpenApiParamsVo implements PublicApiParams {

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
    public String getNeedSignParamString(List<OpenApiParams> extraApiParams) {
        Map<Field, Object> fieldMap = new HashMap<>();

        //1、首先获取本类和子类下的所有fields
        try {
            List<Field> fields = getChildNotNullFields(getClass(), this);
            //添加所有fields到map中
            addAllFieldToMap(fields, this, fieldMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //2、其次获取extraApiParams下的所有fields
        if(!CollectionUtils.isEmpty(extraApiParams)) {
            Map<Field, Object> extraFieldMap = new HashMap<>();
            extraApiParams.forEach(extraApiParam -> {
                try {
                    List<Field> extraFields = getChildNotNullFields(extraApiParam.getClass(), extraApiParam);
                    //添加所有fields到map中
                    addAllFieldToMap(extraFields, extraApiParam, extraFieldMap);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            if(!CollectionUtils.isEmpty(extraFieldMap)) {
                fieldMap.putAll(extraFieldMap);
            }
        }

        List<Field> fields = new ArrayList<>(fieldMap.keySet());
        fields.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(fields)) {
            Field field;
            Object fieldValue;
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                Object object = fieldMap.get(field);
                if (object!=null) {
                    try {
                        fieldValue = getFieldValue(field, object);
                        if (i>0) {
                            sb.append("&");
                        }
                        sb.append(field.getName()).append("=").append(fieldValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return sb.toString();
    }

    private String getFieldValue(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = field.get(object);

        if (fieldValue!=null) {
            if(fieldValue.getClass().isArray()) {
                int length = Array.getLength(fieldValue);
                StringJoiner sj = new StringJoiner(",");
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(fieldValue, i);
                    sj.add(String.valueOf(o));
                }

                return sj.toString();
            }else if(fieldValue instanceof List) {
                return StringUtils.collectionToCommaDelimitedString((List)fieldValue);
            }
        }

        return String.valueOf(fieldValue);
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

    private void addAllFieldToMap(List<Field> fields, Object object, Map<Field, Object> map) {
        if (fields!=null) {
            for (int i = 0; i < fields.size(); i++) {
                map.put(fields.get(i), object);
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
}
