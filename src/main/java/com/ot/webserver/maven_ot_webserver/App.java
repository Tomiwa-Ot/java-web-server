package com.ot.webserver.maven_ot_webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.request.Handler;
import com.ot.webserver.maven_ot_webserver.request.Listener;

public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class);
	
    public static void main(String[] args) {
    	
    	setPort(args);
    	
    	// Shutdown cleanup
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		@Override
    		public void run() {
    			logger.info("Getting ready to shutdown ...");
    		}
    	});
    	
    	ExecutorService requestHandler = Executors.newFixedThreadPool(Config.WORKER_THREADS);
    	
    	try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Config.PORT);
			while(true) {
				Socket socket = serverSocket.accept();
				Listener.getInstance().addRequestToQueue(socket);
				requestHandler.execute(new Handler(Listener.getInstance().handleRequest()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    public static void setPort(String[] args) {
    	if(args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) < Config.PORT_MAX) {
    		Config.PORT = Integer.parseInt(args[0]);
    		logger.info("Changed PORT: " + args[0]);
    	}
    }
}
