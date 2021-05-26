package cn.watsontech.webhelper.openapi.params.base;

import java.util.HashMap;
import java.util.Map;

public class MapOpenApiParams<K,V> extends HashMap<K,V> implements OpenApiParams {
    public MapOpenApiParams() {
        super();
    }

    public MapOpenApiParams(Map<K,V> obj) {
        super();
        this.putAll(obj);
    }
}
