package com.github.jitwxs.sample.protobuf;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.google.protobuf.Message;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Fastjson Protobuf 序列化与反序列化处理器
 *
 * @author jitwxs
 * @date 2021-08-22 19:52
 */
public class ProtobufCodec implements ObjectSerializer, ObjectDeserializer {

    @Override
    public <T> T deserialze(final DefaultJSONParser parser, final Type fieldType, final Object fieldName) {
        final String value = (String) parser.parse();

        if (fieldType instanceof Class && Message.class.isAssignableFrom((Class<?>) fieldType)) {
            return (T) ProtobufUtils.toBean(value, (Class) fieldType);
        }

        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType type = (ParameterizedType) fieldType;

            if (List.class.isAssignableFrom((Class<?>) type.getRawType())) {
                final Type argument = type.getActualTypeArguments()[0];
                if (Message.class.isAssignableFrom((Class<?>) argument)) {
                    return (T) ProtobufUtils.toBeanList(value, (Class) argument);
                }
            }

            if (Map.class.isAssignableFrom((Class<?>) type.getRawType())) {
                final Type[] arguments = type.getActualTypeArguments();
                if (arguments.length == 2) {
                    final Type keyType = arguments[0], valueType = arguments[1];
                    if (Message.class.isAssignableFrom((Class<?>) valueType)) {
                        return (T) ProtobufUtils.toBeanMap(value, (Class) keyType, (Class) valueType);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(final JSONSerializer serializer, final Object object, final Object fieldName,
                      final Type fieldType, final int features) throws IOException {
        final SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if (fieldType instanceof Class && Message.class.isAssignableFrom((Class<?>) fieldType)) {
            final Message value = (Message) object;
            out.writeString(ProtobufUtils.toJson(value));
        } else if (fieldType instanceof ParameterizedType) {
            final ParameterizedType type = (ParameterizedType) fieldType;

            if (List.class.isAssignableFrom((Class<?>) type.getRawType())) {
                final Type argument = type.getActualTypeArguments()[0];
                if (Message.class.isAssignableFrom((Class<?>) argument)) {
                    final List<Message> messageList = (List<Message>) object;
                    out.writeString(ProtobufUtils.toJson(messageList));
                } else {
                    out.writeString("[]");
                }
            } else if (Map.class.isAssignableFrom((Class<?>) type.getRawType())) {
                final Type[] arguments = type.getActualTypeArguments();
                if (arguments.length == 2) {
                    final Type keyType = arguments[0], valueType = arguments[1];

                    if (Message.class.isAssignableFrom((Class<?>) valueType)) {
                        Map<?, Message> messageMap = (Map<?, Message>) object;
                        out.writeString(ProtobufUtils.toJson(messageMap));
                    }
                } else {
                    out.writeString("{}");
                }
            }
        }
    }
}
