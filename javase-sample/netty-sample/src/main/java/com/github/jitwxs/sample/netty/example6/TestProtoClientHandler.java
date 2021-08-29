package com.github.jitwxs.sample.netty.example6;

import com.github.jitwxs.nettystudy.example6.BaseMessageProto;
import com.github.jitwxs.sample.netty.ProtobufUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author jitwxs
 * @date 2021-08-08 14:41
 */
public class TestProtoClientHandler extends SimpleChannelInboundHandler<BaseMessageProto.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessageProto.Student student) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ": " + ProtobufUtils.toJson(student));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(BaseMessageProto.Student.newBuilder().setAge(20).setName("zhangSan").setClass_("大一").build());
    }
}
