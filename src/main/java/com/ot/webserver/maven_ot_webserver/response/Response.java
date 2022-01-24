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
	private static final Logger logger = LogManager.getLogger(Response.class);
	
	public Response(int statusCode, Socket socket) {
		this.statusCode = statusCode;
		this.socket = socket;
	}
	
	public void responseView() {
		try {
			DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
			switch (this.statusCode) {
				case 200:
					
					break;
				case 401:
					outputStream.writeBytes("<b> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</b>");
					outputStream.flush();
					break;			
				case 404:
					outputStream.writeBytes("<b> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</b>");
					outputStream.flush();					
					break;
				case 405:
					outputStream.writeBytes("<b> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</b>");
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
