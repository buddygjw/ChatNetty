package com.netboy.netty.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * TODO
 * Administrator 2013-3-23下午07:50:49
 */
public class RunClient {
    
	public static void main(String[] args) {
		
    	String contextFile = "./conf/spring-client.xml";
    	
		ApplicationContext context = null;
		try {
			context = new FileSystemXmlApplicationContext(contextFile);
		} catch (Exception e) {
			System.out.println("RunMain has some exception");
			e.printStackTrace();
		}
		ClientThread client = (ClientThread)context.getBean("clientThread");
		
		client.init();
		client.start();
	}
}
