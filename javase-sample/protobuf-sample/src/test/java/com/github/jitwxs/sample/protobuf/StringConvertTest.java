package com.github.jitwxs.sample.protobuf;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Protobuf <--> String
 *
 * @author jitwxs
 * @date 2021-08-22 18:43
 */
public class StringConvertTest extends BaseTest {
    /**
     * 原生转换为 String
     */
    @Test
    public void testNativeString() {
        System.out.println(randomUser());
    }

    /**
     * 将单个对象转换为 Json
     */
    @Test
    public void testBean2Json() {
        // 序列化null
        final MessageProto.User nullUser = null;
        final String nullJson = ProtobufUtils.toJson(nullUser);
        assertEquals("", nullJson);

        // 反序列化null
        final MessageProto.User deserializeNull = ProtobufUtils.toBean(nullJson, MessageProto.User.class);
        assertNull(deserializeNull);

        // 序列化
        final MessageProto.User common = randomUser();
        final String commonJson = ProtobufUtils.toJson(common);
        System.out.println(commonJson);

        // 反序列化
        final MessageProto.User deserializeCommon = ProtobufUtils.toBean(commonJson, MessageProto.User.class);
        assertNotNull(deserializeCommon);
        assertEquals(common, deserializeCommon);
    }

    /**
     * 将对象集合转换为 Json
     */
    @Test
    public void testList2Json() {
        // 序列化null集合
        List<MessageProto.User> nullList = null;
        final String nullListJson = ProtobufUtils.toJson(nullList);
        assertEquals("", nullListJson);

        // 反序列化null集合
        final List<MessageProto.User> deserializeNull = ProtobufUtils.toBeanList(nullListJson, MessageProto.User.class);
        assertEquals(0, deserializeNull.size());

        // 序列化空集合
        final String emptyListJson = ProtobufUtils.toJson(Collections.emptyList());
        assertEquals("[]", emptyListJson);

        // 反序列化空集合
        final List<MessageProto.User> deserializeEmpty = ProtobufUtils.toBeanList(emptyListJson, MessageProto.User.class);
        assertEquals(0, deserializeEmpty.size());

        // 序列化集合
        final List<MessageProto.User> commonList = IntStream.range(0, 3).boxed().map(e -> randomUser()).collect(Collectors.toList());
        final String commonListJson = ProtobufUtils.toJson(commonList);
        assertNotEquals("[]", commonListJson);
        System.out.println(commonListJson);

        // 反序列化
        final List<MessageProto.User> deserializeCommon = ProtobufUtils.toBeanList(commonListJson, MessageProto.User.class);
        assertEquals(commonList.size(), deserializeCommon.size());
        for (int i = 0; i < commonList.size(); i++) {
            assertEquals(commonList.get(i), deserializeCommon.get(i));
        }
    }

    /**
     * 将对象Map转换为 Json
     */
    @Test
    public void testMapToJson() {
        // 序列化nullMap
        final Map<Integer, MessageProto.User> nullMap = null;
        final String nullMapJson = ProtobufUtils.toJson(nullMap);
        assertEquals("", nullMapJson);
        // 反序列化nullMap
        final Map<Integer, MessageProto.User> deserializeNull = ProtobufUtils
                .toBeanMap(nullMapJson, Integer.class, MessageProto.User.class);
        assertEquals(0, deserializeNull.size());

        // 序列化空Map
        final String emptyMapJson = ProtobufUtils.toJson(Collections.emptyMap());
        assertEquals("{}", emptyMapJson);

        // 反序列化空Map
        final Map<Integer, MessageProto.User> deserializeEmpty = ProtobufUtils
                .toBeanMap(emptyMapJson, Integer.class, MessageProto.User.class);
        assertEquals(0, deserializeEmpty.size());

        // 序列化Map
        final Map<Integer, MessageProto.User> commonMap = new HashMap<Integer, MessageProto.User>() {{
            put(RandomUtils.nextInt(), randomUser());
            put(RandomUtils.nextInt(), randomUser());
            put(RandomUtils.nextInt(), randomUser());
        }};
        final String commonMapJson = ProtobufUtils.toJson(commonMap);
        assertNotEquals("[]", commonMapJson);
        System.out.println(commonMapJson);

        // 反序列化Map
        final Map<Integer, MessageProto.User> deserializeCommon = ProtobufUtils
                .toBeanMap(commonMapJson, Integer.class, MessageProto.User.class);
        assertEquals(commonMap.size(), deserializeCommon.size());
        commonMap.forEach((k, v) -> assertEquals(v, deserializeCommon.get(k)));
    }
}
