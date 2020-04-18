package com.watsontech.core.service.intf;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Watson on 2020/3/2.
 */
public interface Message {
    Message setState(String name);

    Message setStateDesc(String label);

    Message setCreatedBy(Long id);

    Message setCreatedByName(String username);

    Message setContent(String format);

    Message setUserId(Long id);

    Message setType(String order);

    Message setTitle(String s);

    Message setExtraData(JSONObject jsonObject);
}
