package com.ot.webserver.maven_ot_webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		headers.add("HTTP/1.0 " + Integer.toString(this.statusCode) + " " + Config.STATUS_CODES.get(this.statusCode));
		headers.add("Content-Type: text/html");
		headers.add("Connection: close");
		headers.add("Date: " + formatter.format(new Date(System.currentTimeMillis())));
		headers.add("Server: Simple Java Web Server");
	}
	
	public void responseView() {
		responseHeaders();
		try {
			DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
			switch (this.statusCode) {
				case 200:
					if(isFile) {
						responseBytes(outputStream, this.bytes);
					} else {
						responseBytes(outputStream, this.output.toString());
					}
					break;
				case 401:
					responseBytes(outputStream, "<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");
					outputStream.flush();
					break;			
				case 404:
					responseBytes(outputStream, "<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");				
					break;
				case 405:
					responseBytes(outputStream, "<h1> " + this.statusCode + " " + Config.STATUS_CODES.get(this.statusCode) + "</h1>");
					break;
				default:
					break;
			}
			logger.info("Response sent to " + this.socket.getRemoteSocketAddress());
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void responseBytes(DataOutputStream outputStream, byte[] bytes) throws IOException {
		for(String header : headers) {
			outputStream.writeBytes(header + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body>");
		outputStream.write(bytes);
		outputStream.writeBytes("</body></html>");
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}
	
	public void responseBytes(DataOutputStream outputStream, String response) throws IOException {
		for(String header : headers) {
			outputStream.writeBytes(header + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body>");
		outputStream.writeBytes(response);
		outputStream.writeBytes("</body></html>");
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}

}
