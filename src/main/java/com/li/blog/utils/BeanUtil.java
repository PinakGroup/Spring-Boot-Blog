package com.li.blog.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @since 18-10-23 上午10:11
 **/
public class BeanUtil {

    private static final String serialVersionUID = "serialVersionUID";

    /**
     * @param src   源对象
     * @param des  目标对象
     * @param ignoreFields  忽略字段列表
     */
    public static void transferValue(Object src, Object des, List<String> ignoreFields) {
        Field[] fields = src.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (ignoreFields.contains(field.getName())) {
                continue;
            }
            Object fieldValue = getFieldValue(field.getName(), src);
            if (ObjectUtils.anyNotNull(fieldValue)) {
                setFieldValue(des, field, fieldValue);
            }
        }
    }

    /**`取属性值
     * @param fieldName 属性名
     * @param src    对象
     */
    private static Object getFieldValue(String fieldName, Object src) {
        try {
            if (serialVersionUID.equals(fieldName)) {
                return null;
            }
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = src.getClass().getMethod(getter);
            return method.invoke(src);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** 设置属性值
     * @param des  目标对象
     * @param field 字段
     * @param fieldValue    属性值
     */
    private static void setFieldValue(Object des, Field field, Object fieldValue) {
        String fieldName = field.getName();
        Class[] paramTypes = new Class[1];
        //获取参数类型
        paramTypes[0] = field.getType();
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String setter = "set" + firstLetter + fieldName.substring(1);
            Method method = des.getClass().getMethod(setter, paramTypes);
            method.invoke(des, fieldValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
