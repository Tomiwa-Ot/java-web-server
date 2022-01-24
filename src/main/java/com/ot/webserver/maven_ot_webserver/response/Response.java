package com.ot.webserver.maven_ot_webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ot.webserver.maven_ot_webserver.Config;

public class Response {
	
	private byte[] bytes;
	private boolean isFile;
	private int statusCode;
	private Socket socket;
	private StringBuilder output;
	private ArrayList<String> headers = new ArrayList<String>();
	private static final Logger logger = LogManager.getLogger(Response.class);
	
	public Response(int statusCode, Socket socket) {
		this.statusCode = statusCode;
		this.socket = socket;
	}
	
	public Response(int statusCode, Socket socket, StringBuilder output, boolean isFile) {
		this.statusCode = statusCode;
		this.socket = socket;
		this.output = output;
		this.isFile = isFile;
	}
	
	public Response(int statusCode, Socket socket, byte[] bytes, boolean isFile) {
		this.statusCode = statusCode;
		this.socket = socket;
		this.bytes = bytes;
		this.isFile = isFile;
	}
	
	public void responseHeaders() {
		headers.add(Integer.toString(statusCode) + " " + Config.STATUS_CODES.get(statusCode));
		headers.add("Date: " + new Date(System.currentTimeMillis()));
		headers.add("Server: Simple Java Web Server");
	}
	
	public void responseView() {
		responseHeaders();
		try {
			DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
			for(String header : headers) {
				outputStream.writeBytes(header);
			}
			switch (this.statusCode) {
				case 200:
					if(isFile) {
						outputStream.write(this.bytes);
					} else {
						outputStream.writeBytes(this.output.toString());
					}
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
