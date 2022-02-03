package com.ot.webserver.maven_ot_webserver.request;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.Config;

public class Listener {

	private static final Listener instance = new Listener();
	private static BlockingQueue<Socket> connectionQueue = new ArrayBlockingQueue<Socket>(Config.CONNECTION_QUEUE);
	private static final Logger logger = LogManager.getLogger(Listener.class);
	
	public void addRequestToQueue(Socket s) {
		try {
			logger.info("New connection from " + s.getRemoteSocketAddress() + " added to connection queue");
			connectionQueue.put(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Socket handleRequest() {
		try {
			Socket s = connectionQueue.take();
			logger.info("Procesing " + s.getRemoteSocketAddress());
			return s;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Listener getInstance() {
		return instance;
	}

}
