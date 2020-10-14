package com.lin.missyou.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;

// 单体JSON字符串与Map之间的转换
@Converter
public class MapAndJson implements AttributeConverter<HashMap, String> {

    @Autowired
    private ObjectMapper objectMapper;

    // 将Map转换为数据库中的列(Map->Json)
    @Override
    public String convertToDatabaseColumn(HashMap stringObjectMap) {
        try{
            return objectMapper.writeValueAsString(stringObjectMap);
        }catch(Exception e){
            throw new ServerErrorException(9999);
        }
    }

    // 将数据库中的列转化为实体属性(Json->Map)
    @Override
    public HashMap convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, HashMap.class);
        }catch (Exception e){
            throw new ServerErrorException(9999);
        }
    }
}
