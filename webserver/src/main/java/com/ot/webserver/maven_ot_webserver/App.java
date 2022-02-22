package com.ot.webserver.maven_ot_webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.request.Handler;
import com.ot.webserver.maven_ot_webserver.request.Listener;

public class App {
	
    private static final Logger logger = LogManager.getLogger(App.class);
	
    public static void main(String[] args) {
    	
    	// Load configurations
    	try {
			Config.getInstance().loadConfigurations();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	logger.info("Loading configurations from src/main/resources/properties.json");
    	
    	setPort(args);
    	logger.info("Server is starting ...");
    	

    	
    	// Shutdown cleanup
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		@Override
    		public void run() {
    			logger.warn("Getting ready to shutdown ...");
    		}
    	});
    	
    	ExecutorService requestHandler = Executors.newFixedThreadPool(Config.WORKER_THREADS);
    	
    	try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Config.PORT);
			while(true) {
				Socket socket = serverSocket.accept();
				
//				TODO Implement Basic Authentication Check				
//				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				String line = in.readLine();
//				while(line != null) {
//					if(line.contains("Authorization: Basic" )) {
//						Authenticator.authenticate(socket, line.split("\\s+")[2]);
//						break;
//					}
//					line = in.readLine();
//				}
//		    	if(Config.username != null && Config.password != null) {
//					Authenticator.responseView(socket);
//					continue;
//		    	}
				
				Listener.getInstance().addRequestToQueue(socket);
				requestHandler.execute(new Handler(Listener.getInstance().handleRequest()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    public static void setPort(String[] args) {
    	if(args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) <= Config.PORT_MAX) {
    		Config.PORT = Integer.parseInt(args[0]);
    		logger.info("Changed PORT: " + args[0]);
    	}
    }
    
}
