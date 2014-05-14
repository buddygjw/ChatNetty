package com.netboy.netty.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * TODO
 * Administrator 2013-3-23下午07:49:32
 */
public class RunServer {

	public static void main(String[] args) {
		
    	String contextFile = "./conf/spring-server.xml";
    	
		ApplicationContext context = null;
		try {
			context = new FileSystemXmlApplicationContext(contextFile);
		} catch (Exception e) {
			System.out.println("RunServer has some exception");
			e.printStackTrace();
		}
		final NettyServer server =(NettyServer)context.getBean("nettyServer");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				try {
					server.stop();
				} catch (Exception e) {
					System.out.println("run main stop error!");
				}
			}

		}  );
		server.init();
		server.start();
	}

}
