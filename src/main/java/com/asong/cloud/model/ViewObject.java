package com.asong.cloud.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asong on 2020/1/7
 */

public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
