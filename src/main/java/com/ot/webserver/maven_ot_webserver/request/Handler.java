package com.ot.webserver.maven_ot_webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.Config;
import com.ot.webserver.maven_ot_webserver.response.Response;

public class Handler implements Runnable {
	
	private Socket socket;
	private Response response;
	private static final Logger logger = LogManager.getLogger(Handler.class);
	
	public Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		getHeaders(this.socket);
	}
	
	public void getHeaders(Socket s) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			String requestMethod = in.readLine().split("\\s+")[0];
			if(requestMethod.equalsIgnoreCase(Config.METHODS.GET.toString())) {
				
			} else {
				logger.info(requestMethod.toUpperCase() + ": (405) " + Config.STATUS_CODES.get(405));
				response = new Response(405, this.socket);
				response.responseView();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
