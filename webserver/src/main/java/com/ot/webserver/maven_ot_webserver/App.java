package com.ot.webserver.maven_ot_webserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.request.Handler;
import com.ot.webserver.maven_ot_webserver.request.Listener;

public class App implements Serializable {
	
    private static final Logger logger = LogManager.getLogger(App.class);
    private static boolean[] authResult;
	
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
				if(Config.username != null && Config.password != null) {
					try {
						authResult = authStatus(cloneSocket(socket));
						if(!authResult[0] && !authResult[1]) {
							Authenticator.responseView(socket);
							continue;
						} else if (authResult[0] && !authResult[1]) {
							Authenticator.failedAuthenticationView(socket);
							continue;
						}
							
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
				Listener.getInstance().addRequestToQueue(socket);
				requestHandler.execute(new Handler(Listener.getInstance().handleRequest()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    /**
     * Performs basic authentication on incoming requests
     * 
     * @param socket Connection to perform authentication on
     * @return       Returns a 2 element array of booleans. 
     *               The first for if the request header contains
     *               the Authorization header and the second for
     *               if the supplied credentials are valid.
     * @throws IOException
     */
    public static boolean[] authStatus(Socket socket) throws IOException {
    	boolean[] auth = new boolean[2];
    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line = in.readLine();
		while(line != null) {
			if(line.contains("Authorization: Basic" )) {
				auth[0] = true; auth[1] = Authenticator.authenticate(socket, line.split("\\s+")[2]);
				return auth;
			}
			line = in.readLine();
		}
		auth[0] = false; auth[1] = false;
		return auth;
    }
    
    /**
     * Make a copy of the incoming request object
     * 
     * @param socket The incoming request
     * @return       The copy of the request object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Socket cloneSocket(Socket socket) throws IOException, ClassNotFoundException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(socket);
		oos.flush();
		byte[] bytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return (Socket) new ObjectInputStream(bais).readObject();
    }
    
    /**
     * Sets the port the web server should listen on
     * 
     * @param args
     */
    public static void setPort(String[] args) {
    	if(args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) <= Config.PORT_MAX) {
    		Config.PORT = Integer.parseInt(args[0]);
    		logger.info("Changed PORT: " + args[0]);
    	}
    }
    
}
