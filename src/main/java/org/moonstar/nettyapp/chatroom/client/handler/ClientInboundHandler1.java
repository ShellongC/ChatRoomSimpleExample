package org.moonstar.nettyapp.chatroom.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by shellong on 21/11/2017.
 */
public class ClientInboundHandler1 extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(ClientInboundHandler1.class);
    private String clientName = null;

    public ClientInboundHandler1(String clientName){
        this.clientName = clientName;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bfMsg = (ByteBuf)msg;
        byte[] bytes = new byte[bfMsg.readableBytes()];
        bfMsg.readBytes(bytes);
        String strMsg = new String(bytes);
        logger.info("Client inbound handler get message ["+strMsg+"].");

        System.out.print("Please input some string:");
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String line = bf.readLine();
        String fmsg = clientName + " Say \'"+line+"\'. ";
        ByteBuf encoded = ctx.alloc().buffer(4 * fmsg.length());
        encoded.writeBytes(fmsg.getBytes());
        ctx.writeAndFlush(encoded);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String response = "["+clientName+"] online!";
        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
        encoded.writeBytes(response.getBytes());
        ctx.writeAndFlush(encoded);
        super.channelActive(ctx);
    }
}
