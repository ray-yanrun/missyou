package com.lin.missyou.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

// 数组类型的JSON字符串与List之间的转换
@Converter
public class ListAndJson implements AttributeConverter<List<Object>, String> {

    @Autowired
    private ObjectMapper objectMapper;

    // 将list对象转换为Json字符串
    @Override
    public String convertToDatabaseColumn(List<Object> objects) {
        try{
            return objectMapper.writeValueAsString(objects);
        }catch(Exception e){
            throw new ServerErrorException(9999);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> convertToEntityAttribute(String s) {
        try{
            if(s == null){
                return null;
            }
            return objectMapper.readValue(s, ArrayList.class);
        } catch (Exception e){
            throw new ServerErrorException(9999);
        }
    }
}
