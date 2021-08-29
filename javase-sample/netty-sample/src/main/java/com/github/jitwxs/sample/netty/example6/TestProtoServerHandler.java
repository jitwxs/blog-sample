package com.github.jitwxs.sample.netty.example6;

import com.github.jitwxs.nettystudy.example6.BaseMessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author jitwxs
 * @date 2021-08-08 14:41
 */
public class TestProtoServerHandler extends SimpleChannelInboundHandler<BaseMessageProto.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessageProto.Student student) throws Exception {
        ctx.writeAndFlush(student);
    }
}
