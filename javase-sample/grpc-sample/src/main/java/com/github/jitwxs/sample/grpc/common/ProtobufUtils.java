package com.github.jitwxs.sample.grpc.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Protobuf 序列化与反序列化
 *
 * @author jitwxs
 * @date 2021-08-22 18:30
 */
public class ProtobufUtils {
    private static final JsonFormat.Printer printer;
    private static final JsonFormat.Parser parser;

    static {
        JsonFormat.TypeRegistry registry = JsonFormat.TypeRegistry.newBuilder()
                .add(StringValue.getDescriptor())
                .build();
        printer = JsonFormat
                .printer()
                .usingTypeRegistry(registry)
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace();

        parser = JsonFormat
                .parser()
                .usingTypeRegistry(registry);
    }

    public static String toJson(Message message) {
        if (message == null) {
            return "";
        }

        try {
            return printer.print(message);
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static String toJson(Map<?, ? extends Message> messageMap) {
        if (messageMap == null) {
            return "";
        }
        if (messageMap.isEmpty()) {
            return "{}";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        messageMap.forEach((k, v) -> {
            sb.append("\"").append(JSON.toJSONString(k)).append("\":").append(toJson(v)).append(",");
        });
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }

    public static String toJson(List<? extends MessageOrBuilder> messageList) {
        if (messageList == null) {
            return "";
        }
        if (messageList.isEmpty()) {
            return "[]";
        }

        try {
            StringBuilder builder = new StringBuilder(1024);
            builder.append("[");
            for (MessageOrBuilder message : messageList) {
                printer.appendTo(message, builder);
                builder.append(",");
            }
            return builder.deleteCharAt(builder.length() - 1).append("]").toString();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 反序列化 Message
     *
     * @param json {@link #toJson(Message)} 得到
     */
    public static <T extends Message> T toBean(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            final Method method = clazz.getMethod("newBuilder");
            final Message.Builder builder = (Message.Builder) method.invoke(null);

            parser.merge(json, builder);

            return (T) builder.build();
        } catch (Exception e) {
            throw new RuntimeException("ProtobufUtil toMessage happen error, class: " + clazz + ", json: " + json, e);
        }
    }

    /**
     * 反序列化 Message List
     *
     * @param json {@link #toJson(List)} 得到
     */
    public static <T extends Message> List<T> toBeanList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }

        final JSONArray jsonArray = JSON.parseArray(json);

        final List<T> resultList = new ArrayList<>(jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++) {
            resultList.add(toBean(jsonArray.getString(i), clazz));
        }

        return resultList;
    }

    /**
     * 反序列化 Message Map
     *
     * @param json {@link #toJson(Map)} 得到
     */
    public static <K, V extends Message> Map<K, V> toBeanMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }

        final JSONObject jsonObject = JSON.parseObject(json);

        final Map<K, V> map = Maps.newHashMapWithExpectedSize(jsonObject.size());
        for (String key : jsonObject.keySet()) {
            final K k = JSONObject.parseObject(key, keyClazz);
            final V v = toBean(jsonObject.getString(key), valueClazz);

            map.put(k, v);
        }

        return map;
    }
}
