package com.github.jitwxs.sample.netty.example3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyChatClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap
                    .group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyChatClientInitializer())
                    .connect("localhost", 8899)
                    .sync();

            Channel channel = channelFuture.channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                channel.writeAndFlush(br.readLine() + System.lineSeparator());
            }
        } finally {
            loopGroup.shutdownGracefully();
        }
    }
}
