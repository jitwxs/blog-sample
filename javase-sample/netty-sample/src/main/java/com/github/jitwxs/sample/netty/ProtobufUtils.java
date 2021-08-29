package com.github.jitwxs.sample.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;

public class ProtobufUtils {
    private static final JsonFormat.Printer printer;

    static {
        JsonFormat.TypeRegistry registry = JsonFormat.TypeRegistry.newBuilder()
                .add(StringValue.getDescriptor())
                .build();
        printer = JsonFormat
                .printer()
                .usingTypeRegistry(registry)
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace();
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
}
