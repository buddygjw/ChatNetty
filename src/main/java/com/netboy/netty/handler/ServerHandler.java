package com.netboy.netty.handler;

import java.util.Date;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

/**
 * 服务器端handler
 * TODO
 * Administrator 2013-3-24下午03:34:28
 */
public class ServerHandler extends SimpleChannelHandler {
	private static ChannelGroup channelGroup;

	/**
	 * 构造函数，在spring加载的时候初始化一次。
	 */
	public ServerHandler() {
		super();
		/*获得客户端在服务器端注册的所有信息，用于向所有客户端分发消息*/
		channelGroup = new DefaultChannelGroup("client-channel-group");
	}
/**
 * 用于扑捉客户端退出的消息。
 * 并将其从服务器端的注册表中删掉。
 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		Channel channel = e.getChannel();
		channel.close();
		if (channelGroup.contains(channel)) {
			System.out.println("一个客户端退出："+channel.getId());
			channelGroup.remove(channel);
		}
	}
/**
 * 关键方法
 * 用于接收从客户端发来的消息，进行相应的逻辑处理
 * 这里，我们将任何一个客户端发来的消息，都将其转发给所有的客户端。
 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		String content = (String) e.getMessage();
		System.out.println("服务器收到" + e.getChannel().getId()
				+ " 的消息   时间：" + new Date().toString() + " 消息内容:\n"+ content);
		content=e.getChannel().getId()+":"+content;
		if (content.equalsIgnoreCase("quit")) {
			e.getChannel().close();
			channelGroup.remove(e.getChannel());
			return;
		} else {
			System.out.println("开始转发到其他客户端！:size="+channelGroup.size());
			for(Channel ch:channelGroup){
				System.out.println("开始转发到其他客户端！:"+ch.getId());
				ch.write(content);
			}
		}
	}
/**
 * 对新连接的用户进行注册
 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)throws Exception {
		System.out.println("新的客户端连入："+e.getChannel().getId());
		channelGroup.add(e.getChannel());
	}

	public  ChannelGroup getChannelGroup() {
		return channelGroup;
	}
}