package jit.wxs.grpc.common;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年04月05日 19:36
 */
public class ProtoUtils {
    private static final Logger logger = Logger.getLogger(UserRpcServiceImpl.class.getName());

    private static JsonFormat.TypeRegistry REGISTRY;

    static {
        REGISTRY = JsonFormat.TypeRegistry.newBuilder()
                .add(StringValue.getDescriptor())
                .build();
    }

    public static String toStr(Message message) {
       try {
           return JsonFormat.printer()
                   .usingTypeRegistry(REGISTRY)
                   .includingDefaultValueFields()
                   .omittingInsignificantWhitespace()
                   .print(message);
       } catch (InvalidProtocolBufferException e) {
           logger.log(Level.WARNING, "ProtoUtils#toStr error", e);
       }
       return "";
    }
}
