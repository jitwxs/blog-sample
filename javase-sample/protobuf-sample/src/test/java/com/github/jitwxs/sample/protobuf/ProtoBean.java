package com.github.jitwxs.sample.protobuf;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jitwxs
 * @date 2021-08-22 19:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtoBean {
    private long id;

    @JSONField(serializeUsing = ProtobufCodec.class, deserializeUsing = ProtobufCodec.class)
    private MessageProto.User user;

    @JSONField(serializeUsing = ProtobufCodec.class, deserializeUsing = ProtobufCodec.class)
    private List<MessageProto.User> userList;

    @JSONField(serializeUsing = ProtobufCodec.class, deserializeUsing = ProtobufCodec.class)
    private Map<Integer, MessageProto.User> userMap;

    private Date createDate;
}
