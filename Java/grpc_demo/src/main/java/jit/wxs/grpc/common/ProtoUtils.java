package jit.wxs.grpc.common;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

/**
 * @author jitwxs
 * @date 2020年04月05日 19:36
 */
public class ProtoUtils {
    private static JsonFormat JSON_FORMAT;

    static {
        JSON_FORMAT = new JsonFormat();
    }

    public static String toStr(Message message) {
        return JSON_FORMAT.printToString(message);
    }

}
