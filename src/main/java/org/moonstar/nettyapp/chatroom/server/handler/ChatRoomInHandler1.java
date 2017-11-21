package org.moonstar.nettyapp.chatroom.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.apache.log4j.Logger;

/**
 * Created by shellong on 21/11/2017.
 */
public class ChatRoomInHandler1 extends ChannelInboundHandlerAdapter{
    private static Logger logger = Logger.getLogger(ChatRoomInHandler1.class);

    private ChannelGroup channelGroup;

    public ChatRoomInHandler1(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("Server inbound hander1 activated!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Server inbound channel.channelRead method called!");
        channelGroup.add(ctx.channel());
        ByteBuf bfMsg = (ByteBuf)msg;
        byte[] bytes = new byte[bfMsg.readableBytes()];
        bfMsg.getBytes(bfMsg.readerIndex(), bytes);
        String strMsg = new String(bytes);
        logger.info("Server inbound channel received message["+strMsg+"]");
        channelGroup.writeAndFlush(bfMsg);
    }

}
