package cn.daniel.abridges.converter;

import com.alibaba.fastjson.JSON;

public class JavaScriptConverter {

    /**
     * 对象转json字符串
     *
     * @param object
     * @param type
     * @return
     */
    public String convertToString(Object object, Class<?> type) {
        return JSON.toJSONString(object);
    }
}
