package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author dodo
 * @date 2018/5/20
 * @description
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        // 取消默认转换timestamp形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 所有日期格式统一为以下格式：yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        // 忽略在json字符串中存在，但是在java对象中不存在属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static <T> String obj2String(T obj) {

        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse obj to json error", e);
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {

        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse obj to json error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String obj, Class<T> clazz) {

        if (StringUtils.isEmpty(obj) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) obj : objectMapper.readValue(obj, clazz);
        } catch (IOException e) {
            log.warn("parse string to obj error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String obj, TypeReference<T> reference) {

        if (StringUtils.isEmpty(obj) || reference == null) {
            return null;
        }

        try {
            return (T) (reference.equals(String.class) ? obj : objectMapper.readValue(obj, reference));
        } catch (IOException e) {
            log.warn("parse string to obj error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String obj, Class<?> collectionClass, Class<?> elementClass) {

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);

        try {
            return objectMapper.readValue(obj, javaType);
        } catch (IOException e) {
            log.warn("parse string to obj error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1);
        u1.setEmail("abc@163.com");

        User u2 = new User();
        u1.setId(2);
        u1.setEmail("abc2@163.com");

        String user1Json = JsonUtil.obj2String(u1);
        String user2Json = JsonUtil.obj2StringPretty(u1);

        log.info("user1Json{}", user1Json);
        log.info("user2Json{}", user2Json);

        User user = JsonUtil.stringToObj(user1Json, User.class);

        List<User> l1 = Lists.newArrayList();
        l1.add(u1);
        l1.add(u2);

        String ljson = JsonUtil.obj2StringPretty(l1);

        List<User> jsonl = JsonUtil.stringToObj(ljson, List.class, User.class);

        log.info(ljson);

        System.out.println("end");
    }
}
