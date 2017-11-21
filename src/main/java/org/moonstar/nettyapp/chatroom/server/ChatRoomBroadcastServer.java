package org.moonstar.nettyapp.chatroom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.apache.log4j.Logger;
import org.moonstar.nettyapp.chatroom.server.handler.ChatRoomInHandler1;

import java.net.InetSocketAddress;

/**
 * Created by shellong on 21/11/2017.
 */
public class ChatRoomBroadcastServer {
    private static Logger logger = Logger.getLogger(ChatRoomBroadcastServer.class);

    public static void main(String[] args){
        int port = 9001;
        ServerBootstrap bootserver = new ServerBootstrap();
        final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap b = bootserver.group(eventLoopGroup);
        b = b.channel(NioServerSocketChannel.class);
        b = b.localAddress(new InetSocketAddress(port));
        final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChatRoomInHandler1(channelGroup));
            }
        });

        ChannelFuture f;

        try {
            f = b.bind().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("Error occurred.",e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        logger.error("Chat room server started!");
    }
}
