package cn.watsontech.webhelper.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map工具
 * Created by Watson on 2020/3/19.
 */
public class MapBuilder<K,V> extends HashMap<K,V> {

    public MapBuilder(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapBuilder(int initialCapacity) {
        super(initialCapacity);
    }

    public MapBuilder() {
    }

    public MapBuilder(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public static MapBuilder builder() {
        return new MapBuilder();
    }

    public MapBuilder putNext(K key, V value) {
        super.put(key, value);
        return this;
    }

    public static void main(String[] args) {
        System.out.println(MapBuilder.builder().putNext("key", 1).putNext("value", 2));
    }
}
