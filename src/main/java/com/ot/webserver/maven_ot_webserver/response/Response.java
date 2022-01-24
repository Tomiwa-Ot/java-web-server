package com.ot.webserver.maven_ot_webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.Config;

public class Response {
	
	private int statusCode;
	private Socket socket;
	private StringBuilder output;
	private static final Logger logger = LogManager.getLogger(Response.class);
	
	public Response(int statusCode, Socket socket) {
		this.statusCode = statusCode;
		this.socket = socket;
	}
	
	public Response(int statusCode, Socket socket, StringBuilder output) {
		this.statusCode = statusCode;
		this.socket = socket;
		this.output = output;
	}
	
	public void responseView() {
		try {
			DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
			switch (this.statusCode) {
				case 200:
					outputStream.writeBytes(this.output.toString());
					outputStream.flush();
					break;
				case 401:
					outputStream.writeBytes("<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");
					outputStream.flush();
					break;			
				case 404:
					outputStream.writeBytes("<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");
					outputStream.flush();					
					break;
				case 405:
					outputStream.writeBytes("<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");
					outputStream.flush();
					break;
				default:
					break;
			}
			logger.info("Response sent to " + this.socket.getLocalSocketAddress());
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
