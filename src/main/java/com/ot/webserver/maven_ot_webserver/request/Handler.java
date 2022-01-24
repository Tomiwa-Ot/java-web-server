package com.ot.webserver.maven_ot_webserver.request;

import java.io.BufferedReader;
import java.io.File;
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
			// TODO check for header authorization
			if(requestMethod.equalsIgnoreCase(Config.METHODS.GET.toString())) {
				String uri = in.readLine().split("\\s+")[1];
				if(uri.equals("/")) {
					
				} else {
					File file = new File(uri.substring(1));
					if(file.exists()) {
						logger.info(uri + ": (404) " + Config.STATUS_CODES.get(404));
						response = new Response(404, this.socket);
						response.responseView();
					} else if (file.isDirectory()) {
						StringBuilder output = new StringBuilder("<html><head><title>Index of " + uri.substring(1));
						output.append("</title></head><body><h1>Index of " + uri.substring(1));
						output.append("</h1><hr><pre>");
						File[] files = file.listFiles();
						for (File f : files) {
							output.append(" <a href=\"" + f.getPath() + "\">" + f.getPath() + "</a>\n");
						}
						output.append("<hr></pre></body></html>");
						logger.info(uri + ": (200) " + Config.STATUS_CODES.get(200));
						response = new Response(200, this.socket, output);
						response.responseView();
					} else {
						logger.error(uri + ": (404) " + Config.STATUS_CODES.get(404));
						response = new Response(404, this.socket);
						response.responseView();
					}
				}
			} else {
				logger.error(requestMethod.toUpperCase() + ": (405) " + Config.STATUS_CODES.get(405));
				response = new Response(405, this.socket);
				response.responseView();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
