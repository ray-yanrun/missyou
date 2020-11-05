package com.lin.missyou.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericAndJson {

    private static ObjectMapper objectMapper;

    // 静态属性的注入
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper){
        GenericAndJson.objectMapper = objectMapper;
    }

    // 序列化 对象转JSON字符串
    public static <T> String objectToJson(T obj){
        try{
            return GenericAndJson.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
    }

    // 反序列化  JSON字符串转任意对象(包括List<T>)
    public static <T> T jsonToObject(String s, TypeReference<T> tr){
        try{
            if(s == null){
                return null;
            }
            return GenericAndJson.objectMapper.readValue(s, tr);
        } catch(Exception e) {
            throw new ServerErrorException(9999);
        }
    }

}
