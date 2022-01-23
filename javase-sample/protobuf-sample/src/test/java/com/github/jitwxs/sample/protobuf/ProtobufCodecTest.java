package com.github.jitwxs.sample.protobuf;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProtobufCodecTest extends BaseTest {
    @Test
    public void testCodec() {
        final Map<Integer, MessageProto.User> userMap = new HashMap<Integer, MessageProto.User>() {{
            put(RandomUtils.nextInt(), randomUser());
            put(RandomUtils.nextInt(), randomUser());
            put(RandomUtils.nextInt(), randomUser());
            put(RandomUtils.nextInt(), randomUser());
        }};

        final ProtoBean protoBean = ProtoBean.builder()
                .id(RandomUtils.nextLong())
                .user(randomUser())
                .userList(IntStream.range(0, 3).boxed().map(e -> randomUser()).collect(Collectors.toList()))
                .userMap(userMap)
                .createDate(new Date(RandomUtils.nextLong()))
                .build();

        final String json = JSON.toJSONString(protoBean);

        final ProtoBean protoBean1 = JSON.parseObject(json, ProtoBean.class);

        assertNotNull(protoBean1);
        assertEquals(protoBean.getId(), protoBean1.getId());
        assertEquals(protoBean.getUser(), protoBean1.getUser());
        assertEquals(protoBean.getUserList().size(), protoBean1.getUserList().size());
        for (int i = 0; i < protoBean.getUserList().size(); i++) {
            assertEquals(protoBean.getUserList().get(i), protoBean1.getUserList().get(i));
        }
        assertEquals(protoBean.getUserMap().size(), protoBean1.getUserMap().size());
        protoBean.getUserMap().forEach((k, v) -> assertEquals(v, protoBean1.getUserMap().get(k)));
        assertEquals(protoBean.getCreateDate(), protoBean1.getCreateDate());
    }
}