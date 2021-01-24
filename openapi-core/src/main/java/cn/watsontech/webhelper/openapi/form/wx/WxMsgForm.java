package cn.watsontech.webhelper.openapi.form.wx;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by watson on 2020/4/15.
 */
public class WxMsgForm {

    public WxMsgForm(){}
    public WxMsgForm(List<WxMessageData> data) {
        this.data = data;
    }

    /**
     * 模板内容，不填则下发空模板.
     * <pre>
     * 参数：data
     * 是否必填： 是
     * 描述： 模板内容，不填则下发空模板
     * </pre>
     */
    protected List<WxMessageData> data;

    /**
     * 转换序列号后的data为正确的格式WxMessageData格式
     * 序列化后的为Map格式
     */
    protected List<WxMessageData> autoConvertDataList() {
        if (data!=null) {
            List<WxMessageData> subscribeDataList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                Object dataItem = data.get(i);
                if (dataItem instanceof Map) {
                    WxMessageData subscribeData = new WxMessageData();
                    try {
                        BeanUtils.populate(subscribeData, (Map<String, Object>)dataItem);
                        subscribeDataList.add(subscribeData);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            return subscribeDataList;
        }

        return data;
    }

    public List<WxMessageData> getData() {
        return data;
    }

    public void setData(List<WxMessageData> data) {
        this.data = data;
    }
}
