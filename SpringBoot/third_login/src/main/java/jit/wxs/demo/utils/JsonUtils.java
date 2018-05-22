package jit.wxs.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Json工具类
 * @className JsonUtils.java
 * @author jitwxs
 * @version 创建时间：2018年4月17日 下午3:39:04
 * @dependency jackson-databind
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象-->Json字符串
     * @author jitwxs
     * @version 创建时间：2018年4月17日 下午3:39:35
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json字符串-->对象
     * @author jitwxs
     * @version 创建时间：2018年4月17日 下午3:39:45
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json字符串--> List<对象>
     * @author jitwxs
     * @version 创建时间：2018年4月17日 下午3:40:09
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}