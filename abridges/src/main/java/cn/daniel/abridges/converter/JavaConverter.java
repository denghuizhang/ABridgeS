package cn.daniel.abridges.converter;

import com.alibaba.fastjson.JSON;

public class JavaConverter {
    /**
     * json字符串转为对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public <T> T toObject(String json, Class<T> type) {
        try {
            /*解决fastjson 解析类似["string"]的问题（使用GSON无此问题）*/
            if (type.isAssignableFrom(String.class)) {
                return (T) json;
            }
            return JSON.parseObject(json, type);
        } catch (IllegalStateException e) {
            throw new ConverterException(String.format("Invalid JSON object: %s", json), e);
        }
    }
}
